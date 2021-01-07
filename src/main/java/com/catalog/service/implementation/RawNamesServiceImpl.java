package com.catalog.service.implementation;

import com.catalog.business.repository.RawNamesRepository;
import com.catalog.model.RawNames;
import com.catalog.service.RawNamesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gile on 1/2/2017.
 */
@Service
@Transactional
public class RawNamesServiceImpl implements RawNamesService {

    @Autowired
    RawNamesRepository rawNamesRepository;

    @Override
    public List<RawNames> findByLastAdded(boolean value) {
        return rawNamesRepository.findByLastAdded(true);
    }

    @Override
    public RawNames save(RawNames title){
        return rawNamesRepository.save(title);
    }

    @Override
    public void delete(RawNames title) {
        rawNamesRepository.delete(title);
    }

    @Override
    public void delete(Long id) {
        rawNamesRepository.deleteById(id);
    }

    @Override
    public void updateRawNames(RawNames wiredTitle) {

        RawNames tmp = rawNamesRepository.findById(wiredTitle.getId()).get();
        tmp.setLastAdded(wiredTitle.isLastAdded());
        tmp.setLocation(wiredTitle.getLocation());
        tmp.setName(wiredTitle.getName());
        tmp.setType(wiredTitle.getType());
        rawNamesRepository.save(tmp);
    }

    @Override
    public RawNames find(Long id) {
        return rawNamesRepository.findById(id).get();
    }

    @Override
    public List<RawNames> findAll() {
        Iterable<RawNames> allRecords = rawNamesRepository.findAll();
        List<RawNames> result = new ArrayList<>();

        for ( RawNames name : allRecords ){
            result.add(name);
        }

        return result;
    }
}
