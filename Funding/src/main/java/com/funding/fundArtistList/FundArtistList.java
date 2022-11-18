package com.funding.fundArtistList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.funding.fundArtist.FundArtist;
import com.funding.fundBoard.FundBoard;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
public class FundArtistList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;	// 고유번호
	
	@ManyToOne
	private FundBoard fundBoardId; // 펀딩글
	
	@ManyToOne
	private FundArtist fundArtist; // 공연자
	
}
