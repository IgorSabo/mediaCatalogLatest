package com.catalog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Gile on 3/29/2018.
 */
@Entity
@Table(name = "titleRatings")
@Getter
@Setter
public class TitleRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;
    private String source;

    @ManyToOne(fetch = FetchType.EAGER)
    TitleJsonResponse titleJsonResponse;
}
