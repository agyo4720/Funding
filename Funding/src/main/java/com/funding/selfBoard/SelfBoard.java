package com.funding.selfBoard;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
<<<<<<< HEAD
import javax.persistence.JoinColumn;
=======
>>>>>>> 06d4aa52b968978e032064d67137e33a72aa01b8
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.funding.answer.Answer;
import com.funding.fundArtist.FundArtist;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class SelfBoard {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;			// 고유번호
	
	private String subject;
	
	private String content;
	
	private String filePath;
	
	private String genre;
<<<<<<< HEAD
=======
	
	private Integer starPoint;
	
	@OneToMany(mappedBy = "selfBoard")
	private List<Answer> answer;
>>>>>>> 06d4aa52b968978e032064d67137e33a72aa01b8
	
	private Integer starPoint;
	
	@OneToMany(mappedBy = "selfBoard")
	private List<Answer> answer;
	
	@OneToOne
	@JoinColumn(unique = true)
	private FundArtist fundArtist;
	
}
