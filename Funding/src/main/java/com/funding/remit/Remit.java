package com.funding.remit;


import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name="remit")//회사
public class Remit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; //기본키
	
	@Column(name = "subMallId ")
	private String subMallId ; //서브몰ID
	
	@Column(name = "payoutAmount ")
	private Integer payoutAmount ; //송금금액
	
	@Column(name = "requestedAt ")
	private LocalDateTime requestedAt ; //등록날짜
	
	@Column(name = "payoutDate  ")
	private String  payoutDate  ; //공연완료날짜
	
}
