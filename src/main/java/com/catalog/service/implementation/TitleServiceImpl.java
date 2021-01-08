package com.catalog.service.implementation;

import com.catalog.business.assemblers.TitleJsonResponseAssembler;
import com.catalog.business.assemblers.TitleRatingsAssembler;
import com.catalog.business.dto.TitleDto;
import com.catalog.business.jobs.JobType;
import com.catalog.business.repository.TitleRepository;
import com.catalog.business.systemProcesses.ProcessStatus;
import com.catalog.business.systemProcesses.ProcessType;
import com.catalog.business.titleProcessor.SingleTitleProcessor;
import com.catalog.business.utils.CntTablesManipulator;
import com.catalog.business.utils.Duplicate;
import com.catalog.business.utils.EntityUtil;
import com.catalog.business.utils.FillDatabase;
import com.catalog.model.*;
import com.catalog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.*;

@Repository
@Transactional
public class TitleServiceImpl implements TitleService {

	@Autowired
    TitleRepository titleRepository;

	@Autowired
	NotInsertedService notInsertedService;

	@Autowired
	GenreService genreService;

	@Autowired
	private TitleJsonResponseAssembler titleJsonResponseAssembler;

	@Autowired
	private TitleRatingsAssembler titleRatingsAssembler;

	@Autowired
	private TitleJsonResponseService titleJsonResponseService;

	@Autowired
    ScheduledJobService scheduledJobService;

	@Autowired
	private CntTablesManipulator cntTablesManipulator;

	@Autowired
	private FillDatabase fillDatabase;

	@Autowired
	private RawNamesService rawNamesService;

	@Autowired
	private SystemProcessService systemProcessService;

	@Autowired
	EntityUtil entityUtil;

	private static final DecimalFormat df2 = new DecimalFormat(".##");


	@Value( "${apikey}" )
	private String apiKey;

	@Value( "${apiUrl}" )
	private String apiUrl;

	@Override
	public void processNewTitles( HttpSession session) {


		boolean executeSynchronization = false;
		ScheduledJob activeJob = scheduledJobService.findLastScheduledJob(JobType.SYNCHRONIZATION_JOB);

		SystemProcess syncProcess = null;

		//determine if synchronization job is in progress
		if( activeJob == null || activeJob.getEndTime() != null ){
			executeSynchronization = true;
		}

		//fill db with new names
		fillDatabase.getNames();
		List<RawNames> list = rawNamesService.findByLastAdded(true);
		List<RawNames> totalRawNamesRecords = rawNamesService.findAll();

		//start synchronization only if no active SYNCHRONIZATION_JOB job is found or running
		if( executeSynchronization && ( list.size() > 0 || ( list.size() == 0 && totalRawNamesRecords.size() == 0 ) )  ){

 			if(session != null){
				//create new record in system process table for synchronization process
				syncProcess = systemProcessService.saveSystemProcess(ProcessType.SYNCHRONIZATION_PROCESS);
			}

			double progressIncrement = list.size()/100;

			ExecutorService executor = Executors.newFixedThreadPool(20);
			final ExecutorCompletionService<Map<RawNames, Title>> completionService = new ExecutorCompletionService<>(executor);

			for( RawNames rawName : list ){
				//tasks.add( new SingleTitleProcessor(rawName, typeMap,genreMap, apiKey, apiUrl, session, progressIncrement ));
				completionService.submit(new SingleTitleProcessor(rawName, entityUtil.getTypes(), entityUtil.getGenres(), apiKey, apiUrl));
			}

			try {

				for( int i=0; i< list.size(); i++ ){

					Future<Map<RawNames, Title>> result = completionService.take();
					Map<RawNames, Title> title = (HashMap)  result.get();

					//update progress
					if( session != null ){
						updateProgress(session, progressIncrement, list.size(), title);
					}

					Map.Entry<RawNames, Title> entry = title.entrySet().iterator().next();

					if( entry.getValue() != null ){
						//insert new title
						entry.getValue().toString();

						//save title
						Title response = entry.getValue();
						titleRepository.save(response);

						//creating title json response
						TitleJsonResponse jsonResponse =  titleJsonResponseAssembler.assembleTitleJsonResponseFromJson(response.getApiResponse());

						//creating title ratings for title json response
						Set<TitleRating> ratings = titleRatingsAssembler.assembleRatingsForTitle(response.getApiResponse());

						for( TitleRating rating : ratings ){
							rating.setTitleJsonResponse(jsonResponse);
						}
						jsonResponse.setRatings(ratings);
						jsonResponse.setTitleEntity(response);

						titleJsonResponseService.saveTitleJsonResponse(jsonResponse);
					}
					else{
						//insert into not inserted
						RawNames rawName = entry.getKey();
						NotInserted noIns = new NotInserted();
						noIns.setId(rawName.getId());
						noIns.setLocation(rawName.getLocation());
						noIns.setName(rawName.getName());
						noIns.setType(rawName.getType());
						notInsertedService.save(noIns);
					}

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}

			executor.shutdown();

			if(session != null){
				//updating synchronization system process table
				syncProcess.setEndTime(new Date());
				syncProcess.setProcessStatus(ProcessStatus.FINISHED);
				systemProcessService.saveSystemProcess(syncProcess);
			}


			//updating count tables
			System.out.println("Updating count tables");
			cntTablesManipulator.updateCountTables();
		}
	}

	public synchronized void updateProgress(HttpSession session, double progressIncrement, int listSize, Map<RawNames, Title> title){

		Map.Entry<RawNames, Title> entry = title.entrySet().iterator().next();
		System.out.println(entry.getKey().getName()+" is calling updateProgress()");

		Double status = (Double) session.getAttribute("status");
		if( status != null ){
			status += BigDecimal.valueOf(( (double ) 100 )/ listSize).setScale(3, RoundingMode.HALF_UP).doubleValue();
		}else{
			status = 0.0;
		}
		System.out.println("Setting progress to: "+BigDecimal.valueOf(status).setScale(3, RoundingMode.HALF_UP).doubleValue());
		session.setAttribute("status", BigDecimal.valueOf(status).setScale(3, RoundingMode.HALF_UP).doubleValue());
	}

	@Override
	public String returnProgress(HttpSession session) {

		Double status = (Double) session.getAttribute("status");

		if( status != null ){
			if( status == 100 || status >100 ){
				return "done";
			}
			return df2.format(status);
		}else{
			status = 0.0;
		}

		session.setAttribute("status", status);
		return  df2.format(status);
	}

	@Override
	@Transactional
	public void createTitle(Title title) {

		titleRepository.save(title);
//		Session session = em.unwrap(Session.class);
//		session.save(title);

	} 

	@Override
	@Transactional
	public void updateTitle(Title title) {
		System.out.println(title.getActors());
		System.out.println(title.getDescription());
		System.out.println(title.getImdbTitle());
		System.out.println(title.getRawName());
		System.out.println(title.getLocation());
		System.out.println(title.getQuality());
		System.out.println(title.getPicture());
		System.out.println(title.getGenre());
		System.out.println(title.getType().getId());
		System.out.println(title.getYear().replace("â","–").replace("\u2013","–").replace("â??", "–"));
		
		//Session session = em.unwrap(Session.class);
		
		Optional<Title> titleFromDbOptional = titleRepository.findById(title.getId());

				//session.get(Title.class, title.getIDfilm());
		if( titleFromDbOptional.isPresent() ){
			Title titleFromDb = titleFromDbOptional.get();
			titleFromDb.setActors(title.getActors());
			titleFromDb.setDescription(title.getDescription());
			titleFromDb.setImdbTitle(title.getImdbTitle());
			titleFromDb.setRawName(title.getRawName());
			titleFromDb.setLocation(title.getLocation());
			titleFromDb.setQuality(title.getQuality());
			titleFromDb.setPicture(title.getPicture());
			titleFromDb.setGenre(title.getGenre().replace("-", ""));
			titleFromDb.setYear(title.getYear().replace("â","–").replace("\u2013","–").replace("â??", "–"));
			titleFromDb.setType(title.getType());

			titleRepository.save(titleFromDb);
		}

		//session.update(titleFromDb);
	}

	@Override
	@Transactional
	public Title showInfo(Long titleId) {
		//Session session = em.unwrap(Session.class);

		Optional<Title> optional =titleRepository.findById(titleId);

		Title title = optional.get();
		return title;
	}

	@Override
	@Transactional
	public void addToMustWatch(Long titleId) {
		//Session session = em.unwrap(Session.class);
		Title title = titleRepository.findById(titleId).get();

		title.setMustWatch(true);
		titleRepository.save(title);

	}

	@Override
	@Transactional
	public void addToFavorite(Long titleId) {
		//Session session = em.unwrap(Session.class);
		//Title title = (Title) session.get(Title.class, titleId);
		Title title = titleRepository.findById(titleId).get();
		title.setFavorite(true);
		titleRepository.save(title);
	}

	@Override
	@Transactional
	public void addToIncorrect(Long titleId) {
		//Session session = em.unwrap(Session.class);
		//Title title = (Title) session.get(Title.class, titleId);
		Title title = titleRepository.findById(titleId).get();
		title.setIncorrect(true);
		titleRepository.save(title);

	}

	@Transactional
	@Override
	public void deleteTitle(Long titleId) {
		//Session session = em.unwrap(Session.class);
		Title title = titleRepository.findById(titleId).get();
		titleRepository.delete(title);
		//session.delete(title);

	}

	@Override
	public HashMap<String, Number> getCountsForParams(String genre, String type, String year) {

		return titleRepository.getCountsForParams(genre, type, year);
	}

	@Override
	public Set<TitleDto> getQuickSearchResults(String word) {
		return titleRepository.getQuickSearchResults(word);
	}

	@Override
	public Set<Title> getResults(String type, int page, int perPage, String genre, String year) {
		return titleRepository.getResults(type, page, perPage, genre, year);
	}

	@Override
	public Number getTotalEntities() {
		return titleRepository.getTotalEntities();
	}

	@Override
	public Title getTitle(Long IDtitle) {
		return titleRepository.findById(IDtitle).get();
	}

	@Override
	public Set<Duplicate> getDuplicates() {

		Set<Duplicate> duplicates = new HashSet();
		List<Object[]> list = titleRepository.getDuplicates();
		for(Object[] obj : list){
			duplicates.add(new Duplicate( (long) obj[1], (String) obj[3], (String) obj[2], Long.valueOf((long) obj[0] ).intValue() ));
		}
		return duplicates;
	}

	@Override
	public Set<Title> findByImdbTitle(String title) {
		return titleRepository.findByImdbTitle(title);
	}


}
