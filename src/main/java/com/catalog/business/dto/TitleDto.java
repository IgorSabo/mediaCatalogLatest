package com.catalog.business.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Igor on 1/8/2021.
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TitleDto {

    private Long id;
    private String rawName;
    private String imdbTitle;
    private String genre;
    private String year;
    private String IMDBlink;
    private String location;
    private String description;
    private String quality;
    private String actors;
    private String picture;
    private boolean lastAdded;
    private boolean mustWatch;
    private boolean favorite;
    private boolean incorrect;
    private Float imdbRating;

    public TitleDto(Long id, String rawName, String imdbTitle, String genre, String year, String IMDBlink, String location, String description, String quality, String actors, String picture, boolean lastAdded, boolean mustWatch, boolean favorite, boolean incorrect, Float imdbRating) {
        this.id = id;
        this.rawName = rawName;
        this.imdbTitle = imdbTitle;
        this.genre = genre;
        this.year = year;
        this.IMDBlink = IMDBlink;
        this.location = location;
        this.description = description;
        this.quality = quality;
        this.actors = actors;
        this.picture = picture;
        this.lastAdded = lastAdded;
        this.mustWatch = mustWatch;
        this.favorite = favorite;
        this.incorrect = incorrect;
        this.imdbRating = imdbRating;
    }
}
