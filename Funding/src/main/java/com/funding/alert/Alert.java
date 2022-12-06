package com.funding.alert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
	
	//어떤 알림인지 "댓글", "마감(날짜끝)", "취소(삭제)", "펀딩", "환불(아티스트 없어서)", "수정(아티스트 당선)"
	private String witchAlert;
	
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
