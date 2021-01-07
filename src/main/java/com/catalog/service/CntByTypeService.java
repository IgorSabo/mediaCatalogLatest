package com.catalog.service;

import com.catalog.model.CntByType;

import java.util.List;

/**
 * Created by Gile on 1/2/2017.
 */
public interface CntByTypeService {
    void updateRecord(Long IDType, int count);

    Iterable<CntByType> getCntForAllTypes();
}
