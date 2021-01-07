package com.catalog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Created by Gile on 4/30/2017.
 */
@Entity
@Getter
@Setter
public class MediaFolder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String path;

    @ManyToOne(fetch = FetchType.EAGER)
    private MediaHost mediaHost;

}
