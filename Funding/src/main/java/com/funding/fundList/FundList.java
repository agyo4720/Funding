package com.funding.fundList;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.funding.fundBoard.FundBoard;
import com.funding.fundUser.FundUser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Entity
public class FundList {

	@Id
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fundListId_seq")
//	@SequenceGenerator(sequenceName = "fundListId_seq", allocationSize = 1, name = "fundListId_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 고유번호
	
	@ManyToOne
	private FundBoard fundBoardId; // 어느 펀딩인가?
	
	@ManyToOne
	private FundUser fundUserId; // 누가 펀딩했는가?
	
	
	
}
