package com.funding.fundBoardTarget;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import com.funding.Categorie.Categorie;
import com.funding.fundUser.FundUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundTargetService {

	private final FundTargetRepository fundTargetRepository;
	
	//지정펀딩글 작성(기존이미지로 등록시)
	public void createimg(
			String subject,
			String content,
			String artiest,
			String place,
			String runtime,
			String fundDurationE,
			String startTime,
			Integer minFund,
			Integer fundAmount,
			Categorie categorie,
			String imgPath,
			FundUser user
			) {
		
		
		FundBoardTarget target = new FundBoardTarget();
		target.setSubject(subject);
		target.setContent(content);
		target.setArtiest(artiest);
		target.setPlace(place);
		target.setRuntime(runtime);
		target.setStatus("진행중");
		target.setFundDurationS(LocalDate.now());
		target.setFundDurationE(LocalDate.parse(fundDurationE, DateTimeFormatter.ISO_DATE));
		target.setStartDate(LocalDateTime.parse(startTime));
		target.setCreateDate(LocalDateTime.now());
		target.setMinFund(minFund);
		target.setFundCurrent(0);
		target.setFundAmount(fundAmount);
		target.setCategorie(categorie);
		target.setImgPath(imgPath);
		target.setFundUser(user);
		target.setCurrentMember(0);
		
		fundTargetRepository.save(target);
	}
	
	//지정펀딩글 작성(첨부파일로 등록시)
	public void createfile(
			String subject,
			String content,
			String artiest,
			String place,
			String runtime,
			String fundDurationE,
			String startTime,
			Integer minFund,
			Integer fundAmount,
			Categorie categorie,
			String filePath,
			FundUser user
			) {
		
		
		FundBoardTarget target = new FundBoardTarget();
		target.setSubject(subject);
		target.setContent(content);
		target.setArtiest(artiest);
		target.setPlace(place);
		target.setRuntime(runtime);
		target.setStatus("진행중");
		target.setFundDurationS(LocalDate.now());
		target.setFundDurationE(LocalDate.parse(fundDurationE, DateTimeFormatter.ISO_DATE));
		target.setStartDate(LocalDateTime.parse(startTime));
		target.setCreateDate(LocalDateTime.now());
		target.setMinFund(minFund);
		target.setFundCurrent(0);
		target.setFundAmount(fundAmount);
		target.setCategorie(categorie);
		target.setFilePath(filePath);
		target.setFundUser(user);
		target.setCurrentMember(0);
		
		fundTargetRepository.save(target);
	}
	
	//fundAll(page)
	public Page<FundBoardTarget> findAll(int page){
		Pageable pageable = PageRequest.of(page, 5, Sort.by("createDate").descending());
		Page<FundBoardTarget> targetList = fundTargetRepository.findAll(pageable);
		return targetList;
	}
	
	public List<FundBoardTarget> findAllList(){
		List<FundBoardTarget> targetList = fundTargetRepository.findAll();
		return targetList;
	}
	
	//findById
	public FundBoardTarget findById(Integer id) {
		Optional<FundBoardTarget> fundBoardTarget = fundTargetRepository.findById(id);
		return fundBoardTarget.get();
	}
	
	//해당 카테고리만 가져오기
	public Page<FundBoardTarget> findByCategorie(Categorie categorie, int page){
		Pageable pageable = PageRequest.of(page, 3, Sort.by("createDate").descending());
		Page<FundBoardTarget> targetList = fundTargetRepository.findByCategorie(categorie, pageable);
		return targetList;
	}
	
	
	//결재시 업데이트 됨
	public void addTargetFund(FundBoardTarget fundBoardTarget) {
		fundTargetRepository.save(fundBoardTarget);
	}
	
	
	//지정펀딩 삭제
	public void delete(Integer id) {
		Optional<FundBoardTarget> target = fundTargetRepository.findById(id);
		fundTargetRepository.delete(target.get());
	}
	
}
