package com.funding.fundBoardTarget;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.funding.Categorie.Categorie;
import com.funding.answer.Answer;
import com.funding.answerAns.AnswerAns;
import com.funding.fundUser.FundUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FundBoardTarget {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String subject;
	
	private String content;
	
	private String artiest;

	private String place;
	
	private String runtime;
	
	private String status;
	
	private LocalDate fundDurationS;
	
	private LocalDate fundDurationE;
	
	private LocalDateTime startDate;
	
	private LocalDateTime createDate;
	
	private int view;
	
	private Integer minFund;
	
	private Integer fundCurrent;
	
	private Integer fundAmount;
	
	private String imgPath;
	
	private String filePath;
	
	@ManyToOne
	private FundUser fundUser;
	
	@ManyToOne
	private Categorie categorie;
	
	//@OneToMany(mappedBy = "fundBoardTarget", cascade = CascadeType.REMOVE)
	//private List<FundList> fundList;
	
	//댓글 목록
	@OneToMany(mappedBy = "fundBoardTarget", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;
	
	//답글 목록
	@OneToMany(mappedBy = "fundBoardTarget", cascade = CascadeType.REMOVE)
	private List<AnswerAns> answerAnsList;
	
}
