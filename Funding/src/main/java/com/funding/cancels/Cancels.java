package com.funding.cancels;

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
	
	@Column(name = "canceledAt")
	private LocalDateTime canceledAt; //환불날짜
	
	@Column(name = "cancelReason")
	private String cancelReason; //환불사유
	
	@Column(name = "orderId")
	private String orderId; //주문번호
	
	@Column(name = "payMoney")
	private Integer payMoney; //환불금액
	
	@Column(name = "fundUser")
	private String fundUser; //고객이름
	
	@Column(name = "username")
	private String username; //고객아이디
	
	@Column(name = "orderName")
	private String orderName; //공연이름
}
