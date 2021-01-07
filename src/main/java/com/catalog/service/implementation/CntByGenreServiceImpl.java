package com.catalog.service.implementation;

import com.catalog.business.repository.CntByGenreRepository;
import com.catalog.model.CntByGenre;
import com.catalog.service.CntByGenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Gile on 1/2/2017.
 */

@Repository
@Transactional
public class CntByGenreServiceImpl implements CntByGenreService {

    @Autowired
    CntByGenreRepository cntByGenreRepository;

    @Override
    public void updateRecord(Long IDGenre, int count) {
        CntByGenre genreInfo = cntByGenreRepository.findById(IDGenre).get();
        genreInfo.setTotal(count);
        cntByGenreRepository.save(genreInfo);
    }

    @Override
    public Iterable<CntByGenre> getCntForAllGenres() {
        return (Iterable<CntByGenre>) cntByGenreRepository.findAll();
    }
}
