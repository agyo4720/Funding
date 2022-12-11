package com.funding.sale;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.funding.fundUser.FundUser;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class SaleService {
	private final SaleRepository saleRepository;
	
	//지정공연 결제
	public void targetSaveinfo(String paymentKey, String orederId, int amount, String orderName, 
			Optional<FundUser> FU) {	
		List<Sale> sList = new ArrayList<>(); //결제내역 리스트
		Sale sale = new Sale();
		sale.setFundUser(FU.get().getNickname());//로그인중인 고객이름
		sale.setFundBoardTarget(orderName);//지정공연이름
		sale.setOrderName(orderName);//공연이름
		sale.setPayMoney(amount);//금액
		sale.setOrederId(orederId);//주문번호
		sale.setPayCode(paymentKey);//결제키
		sale.setCheckin("결제완료");//상태 "결제완료"
		sale.setUsername(FU.get().getUsername());//로그인중인 고객아이디 
		sale.setPayDate(LocalDateTime.now());//결제시간
		sList.add(sale);
		saleRepository.save(sale);
	}
	
	//미지정공연 결제
	public void saveinfo(String paymentKey, String orederId, int amount, String orderName, 
			Optional<FundUser> FU) {	
		List<Sale> sList = new ArrayList<>(); //결제내역 리스트
		Sale sale = new Sale();
		sale.setFundUser(FU.get().getNickname());//로그인중인 고객이름
		sale.setFundBoard(orderName);//미지정공연이름
		sale.setOrderName(orderName);//공연이름
		sale.setPayMoney(amount);//금액
		sale.setOrederId(orederId);//주문번호
		sale.setPayCode(paymentKey);//결제키
		sale.setCheckin("결제완료");//상태 "결제완료"
		sale.setUsername(FU.get().getUsername());//로그인중인 고객아이디 
		sale.setPayDate(LocalDateTime.now());//결제시간
		sList.add(sale);
		log.info("sList: "+sList);
		saleRepository.save(sale);
	}
	
	
	//결제리스트 페이징
	public Page<Sale> findByUsername(int page,String user){
		Pageable pageable = PageRequest.of(page, 5, Sort.by("payDate").descending());//결제완료시간순으로 페이징
		Page<Sale> sList = saleRepository.findByUsername(user,pageable);//페이징(아이디일치)
		return sList;
	}
	
	//게시글로 결제목록 불러오기
	public List<Sale> findByFundBoard(String fundBoard){
		List<Sale> sList = saleRepository.findByFundBoard(fundBoard);
		return sList;
	}
}
