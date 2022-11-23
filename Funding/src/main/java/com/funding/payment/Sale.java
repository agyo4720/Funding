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
@Table(name="sale")//회사
public class Sale {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; //기본키
	
	@Column(name = "payCode")
	private String payCode; //결제완료키(환불할때 필요)
	
	@Column(name = "transactionKey")
	private String transactionKey; //거래조회시 필요
	
	@Column(name = "payDate")
	private LocalDateTime payDate; //결제날짜
	
	@Column(name = "orederId")
	private String orederId; //주문번호
	
	@Column(name = "payMoney")
	private Integer payMoney; //결제금액
	
	@Column(name = "fundUser_id")
	private String fundUser_id; //고객이름
	
	@Column(name = "fundBoard_id")
	private String fundBoard_id; //공연이름
}
