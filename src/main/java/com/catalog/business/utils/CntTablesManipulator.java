package com.catalog.business.utils;


import com.catalog.business.repository.TypeRepository;
import com.catalog.model.*;
import com.catalog.service.CntByGenreService;
import com.catalog.service.CntByTypeService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

@Repository
public class CntTablesManipulator {
	
		@PersistenceContext
		private EntityManager em;

	 	@Autowired
	 	private CntByGenreService cntByGenreService;

	 	@Autowired
	 	private CntByTypeService cntByTypeService;

	 	@Autowired
		private EntityUtil entityUtil;

	 	@Autowired
		TypeRepository typeRepository;

	    static HashMap<String, String> genreMap = new HashMap<String, String>();

	    @Transactional
	    public void updateCountTables()
	    {
	    	try
	    	{
				Map<Long, Type> typeMap = entityUtil.getTypes();


	            Session session = em.unwrap(Session.class);
	            Number num;
	            Criteria criteria;
	            ArrayList<CntByGenre> listCntByGenre = (ArrayList<CntByGenre>) cntByGenreService.getCntForAllGenres();
	            ArrayList<CntByType> listCntByType = (ArrayList<CntByType>) cntByTypeService.getCntForAllTypes();
	            //update count table for genres
	            for(Entry<String, String> entry : genreMap.entrySet())
	            {
	            	criteria = session.createCriteria(Title.class).add( Restrictions.like("genre", "%"+entry.getKey()+"%"));
	            	num = (Number)criteria.setProjection(Projections.rowCount()).uniqueResult();
	            	System.out.println("Number of titles for "+entry.getKey()+": "+num.intValue());
	            	for(CntByGenre genreStats : listCntByGenre)
	            	{
	            		if(genreStats.getId()==Long.valueOf(entry.getValue()))
	            		{
	            			if(genreStats.getTotal()!= num.intValue())
	            			{
	            				System.out.println("Found "+genreStats.getTotal()+" values in table for genre: "+entry.getKey()+", updating to "+num.intValue());
                                cntByGenreService.updateRecord(genreStats.getId(), num.intValue());
	            			}
	            		}
	            	}
	            }

	            //update count table for types
	            for(Entry<Long, Type> entry : typeMap.entrySet())
	            {
	            	criteria = session.createCriteria(Title.class).add( Restrictions.eq("IDtype", entry.getKey() ) );
	            	num = (Number)criteria.setProjection(Projections.rowCount()).uniqueResult();
	            	System.out.println("Number of titles for "+entry.getValue()+": "+num.intValue());

	            	for(CntByType typeStats : listCntByType)
	            	{
	            		if(typeStats.getId().equals(Long.valueOf(entry.getKey())));
	            		{
	            			if(typeStats.getTotal()!=num.intValue())
	            			{
	            				System.out.println("Found "+typeStats.getTotal()+" values for type: "+entry.getValue()+", updating to "+num.intValue());
                                cntByTypeService.updateRecord(typeStats.getId(), num.intValue());
	            			}
	            		}
	            	}
	            }
	    	}
	    	catch(Exception e)
	    	{
	    		e.printStackTrace();
	    	}
	    }
}
