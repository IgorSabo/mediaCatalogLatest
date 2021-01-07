package com.catalog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="cntbygenre")
@Getter
@Setter
public class CntByGenre {
	
	@Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private int total;
	private String genre="";
}
