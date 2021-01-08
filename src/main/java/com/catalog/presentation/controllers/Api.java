package com.catalog.presentation.controllers;

import com.catalog.business.assemblers.TitleAssembler;
import com.catalog.business.dto.TitleDto;
import com.catalog.model.Title;
import com.catalog.service.TitleService;
import com.sun.istack.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Igor on 1/7/2021.
 */
@RestController
@RequestMapping("/api")
public class Api {
    @Autowired
    TitleService titleService;

    @Autowired
    TitleAssembler titleAssembler;
    /*@POST
    @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response addTitle(Title title){

        //titleService.createTitle(title);

        return Response.status(Response.Status.CREATED.getStatusCode()).build();
    }*/

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public @ResponseBody Title getTitle(@PathVariable long id){
        return titleService.getTitle(id);
    }

    @RequestMapping(value = "/listResults", method = RequestMethod.GET)
    public @ResponseBody List<TitleDto> getTitles(@Nullable @RequestParam("type") String type,
                                                  @RequestParam("page") int page,
                                                  @RequestParam("perPage") int perPage,
                                                  @Nullable @RequestParam("genre") String genre,
                                                  @Nullable @RequestParam("year") String year){
        Set<Title> results = titleService.getResults(type, Integer.valueOf(page), Integer.valueOf(perPage), genre, year);
        return results.stream().map( d ->  titleAssembler.assmebleTitleDtoFromTitleEntity( d ) ).collect(Collectors.toList());
    }

    @RequestMapping(value = "/findByTitle", method = RequestMethod.GET)
    public @ResponseBody List<Title> findByTitle(@RequestParam("title") String title){
        return titleService.findByImdbTitle(title).stream().collect(Collectors.toList());
    }
}
