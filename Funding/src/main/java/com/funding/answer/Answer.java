package com.funding.answer;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.funding.fundArtist.FundArtist;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundUser.FundUser;
import com.funding.selfBoard.SelfBoard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Answer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String content;
	
	private LocalDateTime createDate;
	
	@ManyToOne
	private FundBoard fundBoard;
	
	@ManyToOne
	private FundBoardTarget fundBoardTarget;
	
	@ManyToOne
	private FundUser fundUser;
	
	@ManyToOne
	private FundArtist fundArtist;
	
	@ManyToOne
	private SelfBoard selfBoard;
	
}
