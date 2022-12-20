package com.funding.fundBoard;

import java.time.LocalDate;
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
import com.funding.Categorie.CategorieRepository;
import com.funding.fundArtist.FundArtistRepository;
import com.funding.fundUser.FundUser;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FundBoardService {

	private final FundBoardRepository fundBoardRepository;
	private final CategorieRepository categorieRepository;
	private final FundArtistRepository fundArtistRepository; 
	
	// 펀드보드 리스트
	public List<FundBoard> findAll(){
		return this.fundBoardRepository.findAll();
	}
	
	// 미지정 펀드 작성(대표이미지로 작성)
	public void createImg(
			String categorieName,
			String subject,
			String content,
			String place,
			String startDateTime,
			String fundDuration,
			String runtime,
			Integer minFund,
			Integer fundAmount,
			String imgPath,
			LocalDateTime createDate,
			FundUser fundUser
			) {
		
		Categorie categorie = this.categorieRepository.findByCategorieName(categorieName).get();
		
		DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		FundBoard fundBoard = new FundBoard();
		
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
		fundBoard.setImgPath(imgPath);
		fundBoard.setCreateDate(LocalDateTime.now());
		fundBoard.setCategorie(categorie);
		fundBoard.setFundUser(fundUser);
		
		this.fundBoardRepository.save(fundBoard);
	}
	
	// 미지정 펀드 작성(파일로 작성)
	public void createFile(
			String categorieName,
			String subject,
			String content,
			String place,
			String startDateTime,
			String fundDuration,
			String runtime,
			Integer minFund,
			Integer fundAmount,
			String filePath,
			LocalDateTime createDate,
			FundUser fundUser
			) {
		
		Categorie categorie = this.categorieRepository.findByCategorieName(categorieName).get();
		
		DateTimeFormatter form = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
		FundBoard fundBoard = new FundBoard();
		
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
		fundBoard.setFilePath(filePath);
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
		
		Pageable pageable = PageRequest.of(page, 5, Sort.by(sorts));
		
		return this.fundBoardRepository.findAll(pageable);
	}
	
	// 펀드보드 카테고리 리스트
	public Page<FundBoard> findByCategorie(Integer page, Categorie categorie){
		
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("createDate"));
		
		Pageable pageable = PageRequest.of(page, 1, Sort.by(sorts));
		
		return this.fundBoardRepository.findByCategorie(pageable, categorie);
	}
	
	// 미지정 펀드 참여하기
	public void create(Integer minFund, Integer star) {
		
		FundBoard fundBoard = new FundBoard();
		
		fundBoard.getMinFund();
		fundBoard.setMinFund(fundBoard.getMinFund() + minFund);

		this.fundBoardRepository.save(fundBoard);
		
	}
	
	// 미지정 펀드 삭제하기
	public void delete(Integer id) {
		this.fundBoardRepository.deleteById(id);
	}
	
	// 결재시 업데이트
	public void addFundBoard(FundBoard fundBoard) {
		fundBoardRepository.save(fundBoard);
	}

	public List<FundBoard> findAllList(){
		List<FundBoard> bList = fundBoardRepository.findAll();
		return bList;
	}
	
	//펀딩 수정
	public void modify(FundBoard fundBoard) {
		fundBoardRepository.save(fundBoard);
	}
}
