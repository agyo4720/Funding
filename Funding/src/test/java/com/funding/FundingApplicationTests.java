package com.funding;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FundingApplicationTests {

	
	@Test
	public void timecheck() {
		LocalDateTime time = null;
		System.out.println("time : " + time);
		String a = "2022-11-24";
		String b = "03:21";
		String result = a + " " +b;
		DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		time = LocalDateTime.parse(result,form);
	}
	

}
