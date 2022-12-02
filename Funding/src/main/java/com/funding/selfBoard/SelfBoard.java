package com.funding.selfBoard;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

import com.funding.fundArtist.FundArtist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SelfBoard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;			// 고유번호
	
	private String subject;
	
	private String content;
	
	private String filePath;
	
	private Integer starPoint;
	
	private String genre;
	
	@OneToOne
	private FundArtist fundArtist;
	
}
