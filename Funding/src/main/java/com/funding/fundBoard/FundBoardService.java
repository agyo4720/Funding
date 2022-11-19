package com.funding.fundBoard;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.funding.Categorie.Categorie;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FundBoardService {

	private final FundBoardRepository fundBoardRepository;
	
//	// 펀드보드 리스트
//	public List<FundBoard> findAll(){
//		return this.fundBoardRepository.findAll();
//	}
	
	// 미지정 펀드 작성
	public void create(
			String subject,
			String content,
			String place,
			String startDate,
			String runtime,
			String fundDuration,
			Integer minFund,
			Integer fundAmount,
			LocalDateTime createDate,
			Categorie categorie) {
		
		FundBoard fundBoard = new FundBoard();
		
		DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		
		fundBoard.setSubject(subject);
		fundBoard.setContent(content);
		fundBoard.setPlace(place);
		fundBoard.setStartDate(LocalDateTime.parse(startDate, form));
		fundBoard.setRuntime(runtime);
		fundBoard.setFundDuration(fundDuration);
		fundBoard.setMinFund(minFund);
		fundBoard.setFundAmount(fundAmount);
		fundBoard.setState("진행중");
		fundBoard.setFundCurrent(0);
		fundBoard.setCurrentMember(0);
		fundBoard.setVote(0);
		fundBoard.setStar(0);
		fundBoard.setCreateDate(LocalDateTime.now());
		fundBoard.setCategorie(categorie);
		
		this.fundBoardRepository.save(fundBoard);
	}
	
	// id로 펀드보드 찾기
	public FundBoard findById(Integer id) {
		Optional<FundBoard> fundBoard = this.fundBoardRepository.findById(id);
		return fundBoard.get();
	}
	
	// 펀드보드 리스트(페이징)
	public Page<FundBoard> findAll(Integer page){
		
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		
		return this.fundBoardRepository.findAll(pageable);
	}
	
}
