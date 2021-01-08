package com.catalog.service.implementation;

import com.catalog.business.assemblers.TitleAssembler;
import com.catalog.business.dto.TitleDto;
import com.catalog.business.repository.TitleRepositoryCustom;
import com.catalog.model.Title;
import com.catalog.model.Type;
import com.catalog.service.TypeService;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thymeleaf.expression.Lists;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Created by Gile on 8/30/2016.
 */
@Repository
@Transactional
public class TitleRepositoryImpl implements TitleRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TitleAssembler titleAssembler;


    @Override
    @Transactional
    public Set<Title> getResults(String type, int page, int perPage, String genre, String year) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Title> titleQuery = cb.createQuery(Title.class);
        Root<Title> t = titleQuery.from( Title.class );

        //predicates
        Predicate predicateForYear = null;
        Predicate predicateForGenre = null;
        Predicate predicateForType = null;
        Predicate predicateForRecentlyAdded = null;
        Predicate predicateForMustWatch = null;
        Predicate predicateForFavorite = null;

        System.out.println("Nadjeni sledeci parametri: type="+type+", page="+page+", perPage="+perPage+", forYear: "+year);
        if(genre!=null && !genre.equals("null") && !genre.equals("undefined"))
        {
            System.out.println("Adding criteria for genre: "+genre);
            predicateForGenre = cb.like(t.get("genre"), "%"+genre+"%");
            //criteria.add(Restrictions.like("genre", "%"+genre+"%"));
        }
        if(year!=null && !year.equals("null") && !year.equals("undefined") && !year.equals("NotSelected"))
        {
            System.out.println("Adding criteria for year: "+year);
            predicateForYear = cb.equal(t.get("year"), year);
            //criteria.add(Restrictions.like("year", "%"+year+"%"));
        }

        List<Type> types = typeService.findAll();
        Join<Title, Type> groupJoinType = t.join("type");

        if(type==null)
        {
            //
        }
        else if(type.toLowerCase().equals("movie"))
        {
            predicateForType = cb.equal( groupJoinType.get("id"),  types.stream().filter( a -> a.getName().equals("movie")).findFirst().get().getId());
            //criteria.add(Restrictions.eq("type_id", types.stream().filter( t -> t.getName().equals("movie")).findFirst().get().getId()));
        }
        else if(type.toLowerCase().equals("series"))
        {
            predicateForType = cb.equal( groupJoinType.get("id"),  types.stream().filter( a -> a.getName().equals("series")).findFirst().get().getId());
            //criteria.add(Restrictions.eq("type_id", types.stream().filter( t -> t.getName().equals("series")).findFirst().get().getId()));
        }
        else if(type.toLowerCase().equals("game"))
        {
            predicateForType = cb.equal( groupJoinType.get("id"),  types.stream().filter( a -> a.getName().equals("game")).findFirst().get().getId());
            //criteria.add(Restrictions.eq("type_id", types.stream().filter( t -> t.getName().equals("game")).findFirst().get().getId()));
        }
        else if(type.toLowerCase().equals("recentlyadded"))
        {
            predicateForRecentlyAdded = cb.equal(t.get("lastAdded"), 1);
            //criteria.add(Restrictions.eq("lastAdded", 1));
        }
        else if(type.toLowerCase().equals("mustwatch"))
        {
            predicateForMustWatch = cb.equal(t.get("mustwatch"), 1);
            //criteria.add(Restrictions.eq("mustWatch", 1));
        }
        else if(type.toLowerCase().equals("favorite"))
        {
            predicateForFavorite = cb.equal(t.get("favorite"), 1);
            //criteria.add(Restrictions.eq("favorite", 1));
        }

        //get final predicate
        titleQuery.where( getFinalPredicate( Arrays.asList( predicateForGenre, predicateForYear, predicateForType, predicateForRecentlyAdded, predicateForMustWatch, predicateForFavorite ), cb ) );

        Query limitedCriteriaQuery = em.createQuery( titleQuery );
        limitedCriteriaQuery.setFirstResult(page);
        limitedCriteriaQuery.setMaxResults(perPage);
        ArrayList<Title> list =(ArrayList<Title>)limitedCriteriaQuery.getResultList();

        HashSet<Title> set = new HashSet<>();
        set.addAll(list);
        return set;
    }

    private Predicate getFinalPredicate(List<Predicate> predicates, CriteriaBuilder cb ){
        Predicate[] allNotNull = predicates.stream().filter( p -> p != null).collect(Collectors.toList()).toArray( new Predicate[0] );
        return cb.and( allNotNull );
    }

    @Override
    @Transactional
    public Set<TitleDto> getQuickSearchResults(String word) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Title> titleQuery = cb.createQuery(Title.class);
        Root<Title> t = titleQuery.from( Title.class );

        Predicate predicateForName = cb.like(t.get("imdbTitle"),"%"+word+"%");
        titleQuery.where(predicateForName);
        Query limitedCriteriaQuery = em.createQuery( titleQuery );

        /*Session session = em.unwrap(Session.class);
        Criteria criteria=session.createCriteria(Title.class);
        ProjectionList plist = Projections.projectionList();
        plist.add(Projections.property("IDfilm"));
        plist.add(Projections.property("imdbTitle"));
        criteria.add(Restrictions.like("imdbTitle", "%"+word+"%"));
        criteria.setProjection(plist);*/

        List<Title> list = (ArrayList<Title>) limitedCriteriaQuery.getResultList();

        System.out.println("Size of the results for word "+word+" is "+list.size());
        return list.stream().map( d -> titleAssembler.assmebleTitleDtoFromTitleEntity(d) ).collect(Collectors.toSet());
    }


    @Override
    @Transactional
    public Number getTotalEntities() {
        Session session = em.unwrap(Session.class);
        Criteria criteria = session.createCriteria(Title.class);
        Number num = (Number)criteria.setProjection(Projections.rowCount()).uniqueResult();
        return num;
    }


    @Transactional
    @Override
    public HashMap<String, Number> getCountsForParams(String genre, String type, String year) {
        Session session = em.unwrap(Session.class);
        Criteria criteria=null;
        criteria=session.createCriteria(Title.class);


        // TODO Auto-generated method stub
        return null;
    }

    /*	@Transactional
	public void synchronizeTitles()
	{
		//napuni bazu sa novim imenima
		fillDatabase.getNames();
		ArrayList<RawNames> list = rawnamesDao.returnLastadded();
		if(list.size()>0)
		{
			createEntities.createTitlesAlt(list);
		}


		//updating count tables
		System.out.println("Updating count tables");
		cntTablesManipulator.updateCountTables();
	}*/

}
