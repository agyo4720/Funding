package com.funding.answerAns;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.funding.fundUser.FundUser;
import com.funding.answer.Answer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class AnswerAns {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne
	private Answer answerId;
	
	@ManyToOne
	private FundUser fundUserId;
}
