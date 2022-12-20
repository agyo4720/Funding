package com.funding.fundBoardTarget;

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

import org.hibernate.annotations.ColumnDefault;

import com.funding.Categorie.Categorie;
import com.funding.alert.Alert;
import com.funding.answer.Answer;
import com.funding.fundTargetList.FundTargetList;
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
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	private String artiest;

	private String place;
	
	private String runtime;
	
	private String status;
	
	private LocalDate fundDurationS;
	
	private LocalDate fundDurationE;
	
	private LocalDateTime startDate;
	
	private LocalDateTime createDate;
	
	private Integer view;
	
	private Integer minFund;
	
	private Integer fundCurrent;
	
	private Integer fundAmount;
	
	private Integer currentMember;
	
	private String imgPath;
	
	private String filePath;
	
	@ManyToOne
	private FundUser fundUser;
	
	@ManyToOne
	private Categorie categorie;
	
	//ManyToMany 대체
	@OneToMany(mappedBy = "fundBoardTarget", cascade = CascadeType.REMOVE)
	private List<FundTargetList> fundTargetList;
	
	//댓글 목록
	@OneToMany(mappedBy = "fundBoardTarget", cascade = CascadeType.REMOVE)
	private List<Answer> answerList;
	
	
	//답글 목록
	@OneToMany(mappedBy = "fundBoardTarget", cascade = CascadeType.REMOVE)
	private List<Alert> alertList;
	
	
	
}
