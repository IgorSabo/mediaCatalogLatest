package com.catalog.business.repository;

import com.catalog.model.Title;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Transactional
public interface TitleRepository extends CrudRepository<Title, Long>, TitleRepositoryCustom {

    @Query("select count(t.imdbTitle), t.id, t.imdbTitle, t.location from Title t GROUP BY t.imdbTitle having count(t.imdbTitle) > 1")
    List<Object[]> getDuplicates();

    Set<Title> findByImdbTitle(String title);

    @Query("select count(t.imdbTitle), t.id, t.imdbTitle, t.location from Title t where t.type.id = :id")
    int getNumberOfTitlesForType(Long id);
}
