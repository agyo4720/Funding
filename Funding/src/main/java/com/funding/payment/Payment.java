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
import com.funding.fundBoardTarget.FundBoardTarget;
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
	
	@Column(name = "payCode")
	private String payCode; //결제키
	
	@Column(name = "payDate")
	private LocalDateTime payDate; //결제날짜
	
	@Column(name = "payMoney")
	private Integer payMoney; //금액
	
	@Column(name = "orederId")
	private String orederId; //주문번호
	
	@Column(name = "status")
	private String status; //상태
	
	@ManyToOne
	private FundUser fundUser; //고객이름
	
	@ManyToOne
	private FundBoard fundBoard; //공연이름
	
	@ManyToOne
	private FundBoardTarget fundBoardTarget; //지정 공연이름
}
