package com.catalog.service;

import com.catalog.model.NotInserted;

import java.util.List;
import java.util.Optional;

/**
 * Created by Gile on 9/23/2016.
 */
public interface NotInsertedService {
    List<NotInserted> getAllResults();
    Optional<NotInserted> findById(Long id);
    void removeFromNotInserted(Long id);
    void save(NotInserted notInserted);
}
