package com.catalog.service;

import com.catalog.model.CntByGenre;

import java.util.List;

/**
 * Created by Gile on 1/2/2017.
 */
public interface CntByGenreService {
    void updateRecord(Long IDType, int count);

    Iterable<CntByGenre> getCntForAllGenres();
}
