package com.catalog.business.utils;

import com.catalog.model.Genre;
import com.catalog.model.Type;
import com.catalog.service.GenreService;
import com.catalog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Igor on 11/28/2020.
 */
@Component
public class EntityUtil implements
        ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    TypeService typeService;

    @Autowired
    GenreService genreService;

    private Map<Long, Type> typeMap = new HashMap<>();
    private Map<String, String> genreMap = new HashMap<>();

    public Map<Long, Type> getTypes(){
        return typeMap;
    }

    public Map<String, String> getGenres(){
        return genreMap;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        //initialize typeMap
        System.out.println("Initializing types...");
        ArrayList<Type> types = typeService.findAll();
        for( Type type : types ){
            typeMap.put(type.getId(), type);
        }
        System.out.println("Done!");

        //initialize genreMap
        System.out.println("Initializing genres...");
        for(Genre genre : genreService.findAll()){
            genreMap.put(genre.getName(), String.valueOf(genre.getId()));
        }
        System.out.println("Done!");
    }
}
