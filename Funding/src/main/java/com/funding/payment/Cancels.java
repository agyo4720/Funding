package com.funding.payment;

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
@Table(name="cancels")//환불
public class Cancels {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; //기본키
	
	@Column(name = "transactionKey")
	private String transactionKey; //거래조회시 필요
	
	@Column(name = "canceledAt")
	private LocalDateTime canceledAt; //환불날짜
	
	@Column(name = "cancelReason")
	private String cancelReason; //환불사유
	
	@Column(name = "fundUser_id")
	private String fundUser_id; //고객이름
	
	@Column(name = "fundBoard_id")
	private String fundBoard_id; //공연이름
}
