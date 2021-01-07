package com.catalog.service;

import com.catalog.model.NotInserted;

import java.util.List;

/**
 * Created by Gile on 9/23/2016.
 */
public interface NotInsertedService {
    List<NotInserted> getAllResults();
    NotInserted getOne(int id);
    void removeFromNotInserted(Long id);
    void save(NotInserted notInserted);
}
