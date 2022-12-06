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
		List<Cancels> cList = new ArrayList<>(); //새로운 리스트선언
		Cancels cancel = new Cancels(); //환불테이블
		cancel.setFundUser(FU.get().getNickname());//로그인중인 이름
		cancel.setUsername(FU.get().getUsername());//로그인중인 아이디
		cancel.setOrderName(orderName);//공연이름
		cancel.setPayMoney(totalAmount);//공연금액
		cancel.setOrderId(orederId);//주문번호
		cancel.setCancelReason(cancelReason);//환불사유
		cancel.setCanceledAt(LocalDateTime.now());//환불한시간
		cList.add(cancel);//리스트에저장
		cancelsRepository.save(cancel);//레포지토리에 저장
		
		List<Sale> sList = saleRepository.findBypayCode(paymentKey);//결제리스트에서 고유 결제키로 정보찾음
		sList.get(0).setCheckin("환불");//상태를 환불로 변경
		sList.get(0).setCancelDate(LocalDateTime.now());//결제리스트에 환불한시간도 저장
		sList.get(0).setCancelReason(cancelReason);//결제리스트에도 환불사유저장
		saleRepository.saveAll(sList);
	}
	
	//게시글 삭제시 자동환불
	public void totalCancelInfo(String orederId, int totalAmount, String orderName, String cancelReason, String fundUser, 
			String username) {
		List<Cancels> cList = new ArrayList<>();
		Cancels cancel = new Cancels();
		cancel.setFundUser(fundUser);//펀딩한 고객이름
		cancel.setUsername(username);//펀딩한 고객아이디
		cancel.setOrderName(orderName);
		cancel.setPayMoney(totalAmount);
		cancel.setOrderId(orederId);
		cancel.setCancelReason(cancelReason);
		cancel.setCanceledAt(LocalDateTime.now());
		cList.add(cancel);
		cancelsRepository.save(cancel);
		
		List<Sale> sList = saleRepository.findByOrederId(orederId);
		for(int i=0; i<sList.size(); i++) { //1명이 아니고 여러명 이라서 for문 사용
			sList.get(i).setCancelReason(cancelReason);
			sList.get(i).setCancelDate(LocalDateTime.now());
			sList.get(i).setCheckin(cancelReason);
			saleRepository.saveAll(sList);
		}
	}
}
