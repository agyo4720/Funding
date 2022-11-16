package com.funding.fundBoard;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FundBoardService {

	private final FundBoardRepository fundBoardRepository;
	
	// 펀딩 목록
	public List<FundBoard> getFundBoard() {	
		return this.fundBoardRepository.findAll();
	}
	
	// 펀딩 작성
	public void create(String subject,
					   String content,
					   String place,
					   LocalDateTime startDate,
					   String runtime,
					   String state,
					   String fundDuration,
					   Integer minFund,
					   Integer fundCurrent,
					   Integer fundAmount,
					   Integer currentMember,
					   Integer vote,
					   Integer star) {
		
		FundBoard fundBoard = new FundBoard();
		
		fundBoard.setSubject(subject);
		fundBoard.setContent(content);
		fundBoard.setPlace(place);
		fundBoard.setStartDate(startDate);
		fundBoard.setRuntime(runtime);
		fundBoard.setState(state);
		fundBoard.setFundDuration(fundDuration);
		fundBoard.setMinFund(minFund);
		fundBoard.setFundCurrent(fundCurrent);
		fundBoard.setFundAmount(fundAmount);
		fundBoard.setCurrentMember(currentMember);
		fundBoard.setVote(vote);
		fundBoard.setStar(star);
		
		this.fundBoardRepository.save(fundBoard);
		
	}
	
	public FundBoard findById(Integer id) {
		Optional<FundBoard> fundBoard = fundBoardRepository.findById(id);
		return fundBoard.get();
	}
	
}
