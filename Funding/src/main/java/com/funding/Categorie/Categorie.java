package com.funding.Categorie;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
public class Categorie {

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categorieId_seq")
//	@SequenceGenerator(sequenceName = "categorieId_seq", allocationSize = 1, name = "categorieId_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 고유번호
	
	private String categoryName; // 카테고리 이름
	
}