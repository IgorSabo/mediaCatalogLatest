package com.catalog.service;

import com.catalog.model.RawNames;

import java.util.List;

/**
 * Created by Gile on 1/2/2017.
 */
public interface RawNamesService {
    List<RawNames> findByLastAdded(boolean value);
    RawNames save(RawNames title);
    void delete(RawNames title);
    void delete(Long IDtitle);

    void updateRawNames(RawNames wiredTitle);

    RawNames find(Long id);
    List<RawNames> findAll();

}
