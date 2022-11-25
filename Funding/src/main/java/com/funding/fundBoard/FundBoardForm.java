package com.funding.fundBoard;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FundBoardForm {
	
	//@NotEmpty(message="카테고리선택은 필수항목입니다.")
	private String categorieName; // 카테고리 이름
	
	//@NotEmpty(message="제목은 필수항목입니다.")
	private String subject; // 제목
	
	//@NotEmpty(message="내용은 필수항목입니다.")
	private String content; // 내용
	
	//@NotEmpty(message="장소는 필수항목입니다.")
	private String place; // 장소
	
	//@NotEmpty(message="공연 시작 일자는 필수항목입니다.")
	private String startDateTime; // 공연 시작 일자
	
	//@NotEmpty(message="공연 시간은 필수항목입니다.")
	private String runtime; // 공연 시간
	
//	private String state; // 펀딩 상태
	
	//@NotEmpty(message="펀딩 기간은 필수항목입니다.")
	private String fundDuration; // 펀딩 기간
	
	//@NotEmpty(message="1인 최소 펀딩 금액은 필수항목입니다.")
	private Integer minFund; // 1인 최소 펀딩 금액
	
//	private Integer fundCurrent; // 펀딩 현재 금액
	
	//@NotEmpty(message="펀딩 목표믁액은 필수항목입니다.")
	private Integer fundAmount; // 펀딩 목표 금액
	
//	private Integer currentMember; // 현재 모집 인원
	
//	private Integer vote; // 별점 투표 수
	
//	private Integer star; // 별점 평균
	
	private LocalDateTime createDate;
	
	
	
	
	
}
