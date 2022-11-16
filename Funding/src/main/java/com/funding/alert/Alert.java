package com.funding.alert;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.funding.fundUser.FundUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity	
public class Alert {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String content;
	
	private String url;
	
	@ManyToOne
	private FundUser fundUser_id;
}
