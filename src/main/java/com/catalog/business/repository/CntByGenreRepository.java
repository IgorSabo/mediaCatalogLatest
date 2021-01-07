package com.catalog.business.repository;

import com.catalog.model.CntByGenre;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

/**
 * Created by Gile on 1/2/2017.
 */
@Transactional
public interface CntByGenreRepository extends CrudRepository<CntByGenre, Long>{


}
