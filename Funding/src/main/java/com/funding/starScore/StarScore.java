package com.funding.starScore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.funding.fundBoard.FundBoard;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Entity
@ToString
public class StarScore {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id; // 고유번호
	
	private Integer star; // 별점
	
	@ManyToOne
	FundBoard fundBoard;
	
}
