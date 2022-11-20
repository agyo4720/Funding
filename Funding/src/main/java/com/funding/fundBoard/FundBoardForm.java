package com.funding.fundBoard;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundBoardForm {

	
	private String subject; // 제목
	
	private String content; // 내용
	
	private String place; // 장소
	
	private String startDate; // 공연 시작 일자
	
	private String startTime; // 공연 시작 시간
	
	private String runtime; // 공연 시간
	
//	private String state; // 펀딩 상태
	
	private String fundDuration; // 펀딩 기간
	
	private Integer minFund; // 1인 최소 펀딩 금액
	
//	private Integer fundCurrent; // 펀딩 현재 금액
	
	private Integer fundAmount; // 펀딩 목표 금액
	
//	private Integer currentMember; // 현재 모집 인원
	
//	private Integer vote; // 별점 투표 수
	
//	private Integer star; // 별점 평균
	
	private LocalDateTime createDate;
	
	
	
	
	
}
