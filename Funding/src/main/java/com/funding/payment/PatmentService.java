package com.funding.payment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundUser.FundUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PatmentService {
	private final SaleRepository saleRepository;
	private final CancelsRepository cancelsRepository;
	
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
		sale.setPayMoney(amount);
		sale.setOrederId(orederId);
		sale.setPayCode(paymentKey);
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
		sale.setPayMoney(amount);
		sale.setOrederId(orederId);
		sale.setPayCode(paymentKey);
		sale.setPayDate(LocalDateTime.now());
		sList.add(sale);
		saleRepository.save(sale);
	}
	
	//지정환불
	public void tarCancelInfo(String orederId, int totalAmount, String orderName, String cancelReason, 
			Optional<FundUser> FU, String paymentKey) {
		log.info("paymentKey: "+paymentKey);
		List<Cancels> cList = new ArrayList<>(); //결제내역 리스트
		Cancels cancel = new Cancels();
		cancel.setFundUser(FU.get().getNickname());
		cancel.setFundBoardTarget(orderName);
		cancel.setPayMoney(totalAmount);
		cancel.setOrderId(orederId);
		cancel.setCancelReason(cancelReason);
		cancel.setCanceledAt(LocalDateTime.now());
		cList.add(cancel);
		cancelsRepository.save(cancel);
		
		List<Sale> sList = saleRepository.findBypayCode(paymentKey);		
		sList.get(0).setCheckin("환불");
		saleRepository.saveAll(sList);
	}
	
	
	
	
	//미지정환불
	public void cancelInfo(String orederId, int totalAmount, String orderName, String cancelReason, 
			Optional<FundUser> FU) {
		List<Cancels> cList = new ArrayList<>(); //결제내역 리스트
		Cancels cancel = new Cancels();
		cancel.setFundUser(FU.get().getNickname());
		cancel.setFundBoard(orderName);
		cancel.setPayMoney(totalAmount);
		cancel.setOrderId(orederId);
		cancel.setCancelReason(cancelReason);
		cancel.setCanceledAt(LocalDateTime.now());
		cList.add(cancel);
		cancelsRepository.save(cancel);
	}
}
