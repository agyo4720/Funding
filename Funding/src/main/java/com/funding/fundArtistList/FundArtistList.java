package com.funding.fundArtistList;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.funding.fundArtist.FundArtist;
import com.funding.fundBoard.FundBoard;
import com.funding.fundUser.FundUser;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class FundArtistList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;	// 고유번호
	
	@ManyToOne
	private FundBoard fundBoard; // 펀딩글
	
	@ManyToOne
	private FundArtist fundArtist; // 공연자
	
	@ManyToMany
	private Set<FundUser> fundUserList; // 누가 투표했는가?
	
}
