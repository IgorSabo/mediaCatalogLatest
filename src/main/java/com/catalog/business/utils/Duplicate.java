package com.catalog.business.utils;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Gile on 5/18/2017.
 */
@Getter
@Setter
public class Duplicate {

    private Long id;
    private int occurence;
    private String location;
    private String name;

    public Duplicate(Long id, String location, String name, int occurence){
        this.id = id;
        this.location = location;
        this.occurence = occurence;
        this.name = name;
    }
}
