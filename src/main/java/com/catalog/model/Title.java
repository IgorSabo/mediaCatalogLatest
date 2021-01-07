/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.catalog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * @author Gile
 */
@Entity
@Getter
@Setter
public class Title {
    @Id
    private Long id;

    @Column(name = "rawname")
    private String rawName;

    @Column(name = "imdbtitle")
    private String imdbTitle;

    @ManyToOne(fetch = FetchType.EAGER)
    private Type type;


    private String genre;
    private String year;
    private String IMDBlink;
    private String location;
    @Lob
    private String description;
    private String quality;
    private String actors;
    private String picture;

    @Column(name = "lastadded")
    private boolean lastAdded;

    @Column(name = "mustwatch")
    private boolean mustWatch;

    private boolean favorite;
    private boolean incorrect;
    private Float imdbRating;

    @Lob
    private String apiResponse;

    @OneToOne(mappedBy = "titleEntity", cascade = CascadeType.REMOVE)
    private TitleJsonResponse titleJsonResponse;

    @Override
    public String toString() {
        return "Title{" +
                "IDfilm=" + id +
                ", rawName='" + rawName + '\'' +
                ", imdbTitle='" + imdbTitle + '\'' +
                ", typeId=" + type.getId() +
                ", genre='" + genre + '\'' +
                ", year='" + year + '\'' +
                ", IMDBlink='" + IMDBlink + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", quality='" + quality + '\'' +
                ", actors='" + actors + '\'' +
                ", picture='" + picture + '\'' +
                ", lastAdded=" + lastAdded +
                ", mustWatch=" + mustWatch +
                ", favorite=" + favorite +
                ", incorrect=" + incorrect +
                ", imdbRating=" + imdbRating +
                ", apiResponse='" + apiResponse + '\'' +
                '}';
    }
}
