package com.funding.fundArtist;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.funding.alert.Alert;
import com.funding.fundArtistList.FundArtistList;

import javax.persistence.OneToOne;

import com.funding.selfBoard.SelfBoard;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
public class FundArtist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;			// 고유번호
	
	@Column(unique=true)
	private String username; 	// 아이디
	private String password; 	// 비밀번호
	private String nickname;	// 이름
	private String email;		// 이메일
	private String mobile;		// 	전화번호
	private String address;		// 주소
	private String gender;		// 성별
	private Date birth;			// 생년월일
	private String role;		// 권한등급
	private Integer likeCount;	// 추천 수

	@OneToOne(mappedBy = "fundArtist")
	private SelfBoard selfBoard;

	@OneToMany(mappedBy = "fundArtist")
	private List<FundArtistList> fundArtistList;
	
	@OneToMany(mappedBy = "guestArtist", cascade = CascadeType.REMOVE)
	private List<Alert> alertList;
}
