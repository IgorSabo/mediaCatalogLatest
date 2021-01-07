package com.catalog.service;

import com.catalog.business.utils.Duplicate;
import com.catalog.model.Title;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Gile on 8/30/2016.
 */
public interface TitleService {

    void createTitle(Title title);

    void updateTitle(Title title);

    Title showInfo(Long titleId);

    void addToMustWatch(Long titleId);

    void addToFavorite(Long titleId);

    void addToIncorrect(Long titleId);

    void deleteTitle(Long titleId);

    HashMap<String, Number> getCountsForParams(String genre, String type, String year);

    //public void synchronizeTitles();

    Set<Object[]> getQuickSearchResults(String word);

    Set<Title> getResults(String type, int page, int perPage, String genre, String year);

    Number getTotalEntities();

    Title getTitle(Long IDtitle);

    Set<Duplicate> getDuplicates();

    Set<Title> findByImdbTitle(String title);

    void processNewTitles(HttpSession session);

    String returnProgress(HttpSession session);

}
