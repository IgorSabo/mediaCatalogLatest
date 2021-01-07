package com.catalog.business.repository;

import com.catalog.model.RawNames;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Gile on 1/2/2017.
 */
@Transactional
public interface RawNamesRepository extends CrudRepository<RawNames, Long>,  RawNamesRepositoryCustom {
    List<RawNames> findByLastAdded(boolean value);
}
