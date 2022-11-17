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
	
	// 펀드보드 리스트
	public List<FundBoard> getFundBoard(){
		return this.fundBoardRepository.findAll();
	}
	
	// 미지정 펀드 작성
	public void create(
			String subject,
			String content,
			String place,
			LocalDateTime startDate,
			String runtime,
			String fundDuration,
			Integer minFund,
			Integer fundAmount) {
		
		FundBoard fundBoard = new FundBoard();
		
		fundBoard.setSubject(subject);
		fundBoard.setContent(content);
		fundBoard.setPlace(place);
		fundBoard.setStartDate(startDate);
		fundBoard.setRuntime(runtime);
		fundBoard.setFundDuration(fundDuration);
		fundBoard.setMinFund(minFund);
		fundBoard.setFundAmount(fundAmount);
		fundBoard.setState("진행중");
		fundBoard.setFundCurrent(0);
		fundBoard.setCurrentMember(0);
		fundBoard.setVote(0);
		fundBoard.setStar(0);
		
		this.fundBoardRepository.save(fundBoard);
	}
	
	public FundBoard findById(Integer id) {
		Optional<FundBoard> fundBoard = this.fundBoardRepository.findById(id);
		return fundBoard.get();
	}
	
}
