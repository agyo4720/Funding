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
	
	//a
	
	//지정공연 결제
	public void targetSaveinfo(String paymentKey, String orederId, int amount, String orderName, 
			Optional<FundUser> FU) {
		CallbackPayload cp = new CallbackPayload(); //정보
		cp.setFundUser(FU.get().getNickname()); 
		cp.setOrderName(orderName);
		cp.setAmount(amount);
		cp.setOrderId(orederId);
		cp.setPaymentKey(paymentKey);
		
		List<Sale> sList = new ArrayList<>(); //결제내역 리스트
		Sale sale = new Sale();
		sale.setFundUser(FU.get().getNickname());
		sale.setFundBoardTarget(orderName);
		sale.setOrderName(orderName);
		sale.setPayMoney(amount);
		sale.setOrederId(orederId);
		sale.setPayCode(paymentKey);
		sale.setCheckin("결제완료");
		sale.setUsername(FU.get().getUsername());
		sale.setPayDate(LocalDateTime.now());
		sList.add(sale);
		saleRepository.save(sale);
	}
	
	//미지정공연 결제
	public void saveinfo(String paymentKey, String orederId, int amount, String orderName, 
			Optional<FundUser> FU) {
		CallbackPayload cp = new CallbackPayload(); //정보
		cp.setFundUser(FU.get().getNickname()); 
		cp.setOrderName(orderName);
		cp.setAmount(amount);
		cp.setOrderId(orederId);
		cp.setPaymentKey(paymentKey);
		
		List<Sale> sList = new ArrayList<>(); //결제내역 리스트
		Sale sale = new Sale();
		sale.setFundUser(FU.get().getNickname());
		sale.setFundBoard(orderName);
		sale.setOrderName(orderName);
		sale.setPayMoney(amount);
		sale.setOrederId(orederId);
		sale.setPayCode(paymentKey);
		sale.setCheckin("결제완료");
		sale.setUsername(FU.get().getUsername());
		sale.setPayDate(LocalDateTime.now());
		sList.add(sale);
		log.info("sList: "+sList);
		saleRepository.save(sale);
	}
	
	
	//결제리스트 페이징
	public Page<Sale> findByUsername(int page,String user){
		Pageable pageable = PageRequest.of(page, 5, Sort.by("payDate").descending());
		Page<Sale> sList = saleRepository.findByUsername(user,pageable);
		return sList;
	}
	

	
	
	
}
