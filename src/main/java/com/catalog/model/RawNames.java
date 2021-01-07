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
 *
 * @author Gile
 */
@Entity
@Table(name="rawnames")
@Getter
@Setter
public class RawNames {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String type;
    
    @Column(name = "path")
    String location;
    
    @Column(name = "lastadded")
    private boolean lastAdded;
}
