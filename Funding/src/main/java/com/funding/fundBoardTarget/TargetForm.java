package com.funding.fundBoardTarget;

import java.time.LocalDate;

import javax.persistence.ManyToOne;

import com.funding.fundUser.FundUser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class TargetForm {
	
	
	private String subject;
	
	private String content;
	
	private String Aertiest;
	
	private String place;
	
	private String runtime;

	private String fundDurationE;
	
	private String startDate;
		
	private Integer minFund;
	
	private Integer fundAmount;
	
}
