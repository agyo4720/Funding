package com.funding.payment;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.funding.fundBoard.FundBoard;
import com.funding.fundUser.FundUser;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name="payment")//고객
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Integer id; //기본키
	
	@Column(name = "payDate")
	private LocalDateTime payDate; //결제날짜
	
	@Column(name = "orederId")
	private String orederId; //주문번호
	
	@Column(name = "payMoney")
	private Integer payMoney; //금액
	
	@ManyToOne
//	@Column(name = "fundUser_id")
	private FundUser fundUser_id; //고객이름
	
	@ManyToOne
//	@Column(name = "fundBoard_id")
	private FundBoard fundBoard_id; //공연이름
	
}
