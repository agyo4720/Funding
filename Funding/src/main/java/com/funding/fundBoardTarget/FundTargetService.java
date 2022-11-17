package com.funding.fundBoardTarget;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.funding.Categorie.Categorie;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundTargetService {

	private final FundTargetRepository fundTargetRepository;
	
	//지정펀딩글 작성
	public void create(
			String subject,
			String content,
			String artiest,
			String place,
			String runtime,
			String fundDurationE,
			String startTime,
			Integer minFund,
			Integer fundAmount,
			Categorie categorie
			) {
		
		DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		FundBoardTarget target = new FundBoardTarget();
		target.setSubject(subject);
		target.setContent(content);
		target.setArtiest(artiest);
		target.setPlace(place);
		target.setRuntime(runtime);
		target.setStatus("진행중");
		target.setFundDurationS(LocalDate.now());
		target.setFundDurationE(LocalDate.parse(fundDurationE, DateTimeFormatter.ISO_DATE));
		target.setStartDate(LocalDateTime.parse(startTime, form));
		target.setMinFund(minFund);
		target.setFundCurrent(0);
		target.setFundAmount(fundAmount);
		target.setCategorie(categorie);
		
		fundTargetRepository.save(target);
	}
	
}