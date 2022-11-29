package com.funding.fundBoard;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.funding.Categorie.Categorie;
import com.funding.Categorie.CategorieRepository;
import com.funding.fundUser.FundUser;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FundBoardService {

	private final FundBoardRepository fundBoardRepository;
	private final CategorieRepository categorieRepository;
	
	// 펀드보드 리스트
	public List<FundBoard> findAll(){
		return this.fundBoardRepository.findAll();
	}
	
	// 미지정 펀드 작성
	public void create(
			String categorieName,
			String subject,
			String content,
			String place,
			String startDateTime,
			String fundDuration,
			String runtime,
			Integer minFund,
			Integer fundAmount,
			LocalDateTime createDate,
			String imgPath,
			FundUser fundUser
			) {
		
		FundBoard fundBoard = new FundBoard();
		
		Categorie categorie = this.categorieRepository.findByCategorieName(categorieName).get();
		
		DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		fundBoard.setCategorieName(categorieName);
		fundBoard.setSubject(subject);
		fundBoard.setContent(content);
		fundBoard.setPlace(place);
		fundBoard.setStartDateTime(LocalDateTime.parse(startDateTime));
		fundBoard.setFundDuration(LocalDate.parse(fundDuration, form));
		fundBoard.setRuntime(runtime);
		fundBoard.setMinFund(minFund);
		fundBoard.setFundAmount(fundAmount);
		fundBoard.setState("진행중");
		fundBoard.setFundCurrent(0);
		fundBoard.setCurrentMember(0);
		fundBoard.setVote(0);
		fundBoard.setStar(0);
		fundBoard.setImgPath(imgPath);
		fundBoard.setCreateDate(LocalDateTime.now());
		fundBoard.setCategorie(categorie);
		fundBoard.setFundUser(fundUser);
		
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
		
		Pageable pageable = PageRequest.of(page, 1, Sort.by(sorts));
		
		return this.fundBoardRepository.findAll(pageable);
	}
	
	// 펀드보드 카테고리 리스트
	public Page<FundBoard> findByCategorie(Integer page, Categorie categorie){
		
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		
		Pageable pageable = PageRequest.of(page, 3, Sort.by(sorts));
		
		return this.fundBoardRepository.findByCategorie(pageable, categorie);
	}
	
	// 미지정 펀드 참여하기
	public void create(Integer minFund, Integer star) {
		
		FundBoard fundBoard = new FundBoard();
		
		fundBoard.getMinFund();
		fundBoard.getStar();
		fundBoard.setMinFund(fundBoard.getMinFund() + minFund);
		fundBoard.setStar(fundBoard.getStar() + star);

		this.fundBoardRepository.save(fundBoard);
		
	}
	
	// 미지정 펀드 삭제하기
	public void delete(Integer id) {
		this.fundBoardRepository.deleteById(id);
	}

}
