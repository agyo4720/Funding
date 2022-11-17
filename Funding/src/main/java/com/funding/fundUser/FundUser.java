package com.funding.fundUser;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FundUser {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String username;
	private String password;
	private String nickname;
	private String email;
	private String mobile;
	private String address;
	private String gender;
	private String birth;
	private String role;
	
//	private List<FundBoard> FundBoard;
//	private List<Payment> Payment;
//	private List<Answer> Answer;
//	private List<AnswerAns> AnswerAns;
//	private List<FundList> FundList;
}
