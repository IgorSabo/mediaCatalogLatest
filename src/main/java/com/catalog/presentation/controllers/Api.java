package com.catalog.presentation.controllers;

import com.catalog.model.Title;
import com.catalog.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Igor on 1/7/2021.
 */
@RestController
public class Api {
    @Autowired
    TitleService titleService;


    /*@POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response addTitle(Title title){

        //titleService.createTitle(title);

        return Response.status(Response.Status.CREATED.getStatusCode()).build();
    }*/

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Title getTitle(@PathVariable long id){
        return titleService.getTitle(id);
    }

    @RequestMapping(value = "/listResults", method = RequestMethod.GET)
    public List<Title> getTitles(@RequestParam("type") String type,
                                 @RequestParam("page") int page,
                                 @RequestParam("perPage") String perPage,
                                 @RequestParam("genre") String genre,
                                 @RequestParam("year") String year){
        return titleService.getResults(type, Integer.valueOf(page), Integer.valueOf(perPage), genre, year).stream().collect(Collectors.toList());
    }

    @RequestMapping(value = "/findByTitle", method = RequestMethod.GET)
    public List<Title> findByTitle(@RequestParam("title") String title){
        return titleService.findByImdbTitle(title).stream().collect(Collectors.toList());
    }
}
