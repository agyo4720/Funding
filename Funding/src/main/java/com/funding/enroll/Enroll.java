package com.funding.enroll;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="enroll")//회사
public class Enroll {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; //기본키
	
	@Column(name = "subMallId ")
	private String subMallId ; //서브몰ID
	
	@Column(name = "companyName ")
	private String companyName ; //상호명
	
	@Column(name = "representativeName ")
	private String representativeName ; //대표자명
	
	@Column(name = "businessNumber ")
	private String businessNumber ; //사업자등록번호
	
	@Column(name = "bank ")
	private String bank ; //은행
	
	@Column(name = "accountNumber ")
	private String accountNumber ; //계좌번호
}
