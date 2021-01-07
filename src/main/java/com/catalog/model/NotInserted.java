package com.catalog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="notinserted")
@Getter
@Setter
public class NotInserted {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	private String name;
    private String type;
    @Column(name = "path")
    private String location;
    
    @Column(name = "lastadded")
    private boolean lastAdded;

}
