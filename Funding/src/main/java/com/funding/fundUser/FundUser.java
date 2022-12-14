package com.funding.fundUser;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.funding.alert.Alert;
import com.funding.answer.Answer;
import com.funding.fundBoard.FundBoard;
import com.funding.fundList.FundList;
import com.funding.fundTargetList.FundTargetList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
public class FundUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;				// 고유번호

	@Column(unique=true)
	private String username;		// 아이디
	private String password;		// 비밀번호
	private String nickname;		// 이름
	private String email;			// 이메일
	private String mobile;			// 전화번호
	private String address;			// 주소
	private String gender;			// 성별
	private Date birth;				// 생년월일
	private String role;			// 권한등급

	@OneToMany(mappedBy = "fundUser", cascade = CascadeType.REMOVE)
	private List<FundBoard> FundBoard;

	@OneToMany(mappedBy = "fundUser", cascade = CascadeType.REMOVE)
	private List<Answer> Answer;

	@OneToMany(mappedBy = "fundUser", cascade = CascadeType.REMOVE)
	private List<FundList> FundList;

	@OneToMany(mappedBy = "fundUser", cascade = CascadeType.REMOVE)
	private List<FundTargetList> fundTargetList;
	
	@OneToMany(mappedBy = "guestUser", cascade = CascadeType.REMOVE)
	private List<Alert> alertList;
	
}
