package com.funding.answer;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.funding.FundUser.FundUser;
import com.funding.answerAns.AnswerAns;
import com.funding.fundBoard.FundBoard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne
	private FundBoard fundBoard;
	
	@ManyToOne
	private FundUser fundUserId;
	
	@OneToMany(mappedBy = "answerId", cascade = CascadeType.REMOVE)
	private List<AnswerAns> answerAnsList;
}
