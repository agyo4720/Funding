package com.funding.fundArtist;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class FundArtist {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String username;
	private String password;
	private String nickname;
	private String email;
	private String mobile;
	private String role;
	private Integer likeCount;
}
