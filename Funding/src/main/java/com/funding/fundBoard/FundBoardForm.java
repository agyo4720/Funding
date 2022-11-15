package com.funding.fundBoard;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundBoardForm {
	
	@NotEmpty(message=" 제목은 필수항목입니다.")
	private String subject;
	
	@NotEmpty(message="내용은 필수항목입니다.")
	private String content;
	
	@NotEmpty(message="장소은 필수항목입니다.")
	private String place;
	
	private LocalDateTime startDate;
	
	private String runtime;
	
	private String state;
	
	private String fundDuration;
	
	private Integer minFund;
	
	private Integer fundCurrent;
	
	private Integer fundAmount;
	
	private Integer currentMember;
	
	private Integer vote;
	
	private Integer star;
	
}
