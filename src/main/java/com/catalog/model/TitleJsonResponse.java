package com.catalog.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Gile on 3/29/2018.
 */
@Entity
@Table(name = "titleJsonResponse")
@Getter
@Setter
public class TitleJsonResponse{

    @Id
    @Column(name = "id", unique = true, nullable = false)
    @GeneratedValue(generator = "gen")
    @GenericGenerator(name = "gen", strategy = "foreign", parameters = {@Parameter(name = "property", value="titleEntity")})
    private Long id;

    private String metascore;
    private String boxOffice;
    private String webSite;
    private String imdbRating;
    private String imdbVotes;

    @OneToMany(mappedBy = "titleJsonResponse", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @MapKeyJoinColumn(name = "titleJsonResponseId")
    private Set<TitleRating> ratings;

    @OneToOne
    @PrimaryKeyJoinColumn
    private Title titleEntity;

    private String runTime;
    private String language;
    private String rated;
    @Lob
    private String production;
    private String released;
    private String imdbId;
    @Lob
    private String plot;
    private String director;
    private String title;
    private String actors;
    private String type;
    private String awards;
    private String dvd;
    private String year;
    private String poster;
    private String country;
    private String genre;
    @Lob
    private String writer;

}
