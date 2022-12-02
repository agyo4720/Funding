package com.funding.cancels;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundUser.FundUser;
import com.funding.sale.Sale;
import com.funding.sale.SaleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CancelsService {
	private final SaleRepository saleRepository;
	private final CancelsRepository cancelsRepository;
	
	//환불
	public void cancelInfo(String orederId, int totalAmount, String orderName, String cancelReason, 
			Optional<FundUser> FU, String paymentKey) {
		List<Cancels> cList = new ArrayList<>(); //결제내역 리스트
		Cancels cancel = new Cancels();
		cancel.setFundUser(FU.get().getNickname());
		cancel.setOrderName(orderName);
		cancel.setPayMoney(totalAmount);
		cancel.setOrderId(orederId);
		cancel.setCancelReason(cancelReason);
		cancel.setCanceledAt(LocalDateTime.now());
		cList.add(cancel);
		cancelsRepository.save(cancel);
		
		List<Sale> sList = saleRepository.findBypayCode(paymentKey);		
		sList.get(0).setCheckin("환불");
		sList.get(0).setCancelDate(LocalDateTime.now());
		sList.get(0).setCancelReason(cancelReason);
		saleRepository.saveAll(sList);
	}
	
	public void totalCancelInfo(String orederId, int totalAmount, String orderName, String cancelReason, String FundUser) {
		List<Cancels> cList = new ArrayList<>(); //결제내역 리스트
		Cancels cancel = new Cancels();
		cancel.setFundUser(FundUser);
		cancel.setOrderName(orderName);
		cancel.setPayMoney(totalAmount);
		cancel.setOrderId(orederId);
		cancel.setCancelReason(cancelReason);
		cancel.setCanceledAt(LocalDateTime.now());
		cList.add(cancel);
		cancelsRepository.save(cancel);
	}
}
