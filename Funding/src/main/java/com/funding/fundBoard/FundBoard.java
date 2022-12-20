package com.funding.fundBoard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.funding.Categorie.Categorie;
import com.funding.alert.Alert;
import com.funding.answer.Answer;
import com.funding.fundArtist.FundArtist;
import com.funding.fundArtistList.FundArtistList;
import com.funding.fundList.FundList;
import com.funding.fundUser.FundUser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@Entity
public class FundBoard {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;	// 고유번호
	
	private String CategorieName; // 카테고리 이름
	
	private String subject; // 제목
	
	@Column(columnDefinition = "TEXT")
	private String content; // 내용
	
	private String place; // 장소
	
	private LocalDateTime startDateTime; // 공연 시작 일자
	
	private LocalDate fundDuration; // 펀딩 기간
	
	private String runtime; // 공연 시간
	
	private String state; // 펀딩 상태
	
	private Integer minFund; // 1인 최소 펀딩 금액
	
	private Integer fundCurrent; // 펀딩 현재 금액
	
	private Integer fundAmount; // 펀딩 목표 금액

	private Integer currentMember; // 현재 모집 인원
	
	private LocalDateTime createDate; // 작성일시
	
	private String imgPath;
	
	private String filePath;
	
	// 제약 조건
	@ManyToOne
	private FundUser fundUser; // 작성자 유저
	
	@ManyToOne
	private Categorie categorie; // 카테고리
	
	@OneToOne
	private FundArtist fundArtist; // 최종 선정된 공연자
	
	@OneToMany(mappedBy = "fundBoard", cascade = CascadeType.REMOVE) 
	private List<FundList> fundList; // 펀딩한 유저 목록
	
	@OneToMany(mappedBy = "fundBoard", cascade = CascadeType.REMOVE)
	private List<FundArtistList> fundArtistList; // 신청한 공연자 목록
	
	@OneToMany(mappedBy = "fundBoard", cascade = CascadeType.REMOVE)
	private List<Answer> answerList; // 질문들
	
	//답글 목록
	@OneToMany(mappedBy = "fundBoard", cascade = CascadeType.REMOVE)
	private List<Alert> alertList;
	
}
