package com.catalog.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name="cntbytype")
public class CntByType {
	@Id 
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private int total;
	private String type="";
}
