package com.funding.alert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.funding.fundArtist.FundArtist;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundUser.FundUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity	
public class Alert {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String content;
	
	private String url;
	
	//알림 받을 사람
	@ManyToOne
	private FundUser hostUser;
	
	//알림 받을 사람
	@ManyToOne
	private FundArtist hostArtist;
	
	//알림 발생 사람
	@ManyToOne
	private FundUser guestUser;
	
	//알림 발생 사람
	@ManyToOne
	private FundArtist guestArtist;
	
	
	//알림 발생지
	@ManyToOne
	private FundBoardTarget fundBoardTarget;
	
	@ManyToOne
	private FundBoard fundBoard;
	
	
}
