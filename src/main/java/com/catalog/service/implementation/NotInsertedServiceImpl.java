package com.catalog.service.implementation;

import com.catalog.business.repository.NotInsertedRepository;
import com.catalog.model.NotInserted;
import com.catalog.service.NotInsertedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

/**
 * Created by Gile on 9/23/2016.
 */
@Repository
@Transactional
public class NotInsertedServiceImpl implements NotInsertedService {

    @Autowired
    NotInsertedRepository notInsertedRepository;

    @Override
    @Transactional
    public List<NotInserted> getAllResults() {
        return (List<NotInserted>) notInsertedRepository.findAll();
    }

    @Override
    public Optional<NotInserted> findById(Long id) {
        return notInsertedRepository.findById(id);
    }

    @Override
    @Transactional
    public void removeFromNotInserted(Long id) {
        notInsertedRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void save(NotInserted notInserted) {
        notInsertedRepository.save(notInserted);
    }
}
