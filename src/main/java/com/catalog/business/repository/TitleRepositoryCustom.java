package com.catalog.business.repository;

import com.catalog.business.dto.TitleDto;
import com.catalog.model.Title;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Set;

@Transactional
public interface TitleRepositoryCustom {

	HashMap<String, Number> getCountsForParams(String genre, String type, String year);
	
	//public void synchronizeTitles();
	
	Set<TitleDto> getQuickSearchResults(String word);

	Set<Title> getResults(String type, int page, int perPage, String genre, String year);

	Number getTotalEntities();

}
