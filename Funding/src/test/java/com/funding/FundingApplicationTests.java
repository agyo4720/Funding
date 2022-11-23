package com.funding;


import java.sql.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.funding.fundArtist.FundArtistRepository;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserRepository;
import com.funding.fundUser.FundUserService;

@SpringBootTest
class FundingApplicationTests {
	
	@Autowired
	FundUserService fundUserService;
	@Autowired
	FundUserRepository fundUserRepository;
	@Autowired
	FundArtistService fundArtistService;
	@Autowired
	FundArtistRepository fundArtistRepository;
	
	@Test
	public void dateCheck() {
		FundUser FU = new FundUser();
		
		FU.setBirth(Date.valueOf("1996-01-01"));
		
		this.fundUserRepository.save(FU);
	}
	
	
//	@Test
//	public void timecheck() {
//		LocalDateTime time = null;
//		System.out.println("time : " + time);
//		String a = "2022-11-24";
//		String b = "03:21";
//		String result = a + " " +b;
//		DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
//		
//		time = LocalDateTime.parse(result,form);
//	}
	

}
