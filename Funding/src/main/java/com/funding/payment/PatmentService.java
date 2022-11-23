package com.funding.payment;

import java.net.URI;
import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class PatmentService {
	private final SaleRepository saleRepository;
	private final PaymentRepository paymentRepository;
	
	public void savecreditinfo(String paymentKey, String orederId, int amount, String orderName,
			String status) {
		
		CallbackPayload cp = new CallbackPayload(); //정보
		cp.setFundUser("이토페2"); 
		cp.setOrderName(orderName);
		cp.setAmount(amount);
		cp.setOrderId(orederId);
		cp.setPaymentKey(paymentKey);
		cp.setStatus(status);
		
		List<Sale> sList = new ArrayList<>(); //고객이 계정삭제해도 결제내역은 남아있는 리스트
		Sale sale = new Sale();
		sale.setFundUser("이토페2");
		sale.setFundBoard(orderName);
		sale.setPayMoney(amount);
		sale.setOrederId(orederId);
		sale.setPayCode(paymentKey);
		sale.setPayDate(LocalDateTime.now());
		sList.add(sale);
		saleRepository.save(sale);
		
		List<Payment> pList = new ArrayList<>(); //고객이 보는 테이블
		Payment payment = new Payment();
		payment.setFundUser(null);
		payment.setFundBoard(null);
		payment.setPayMoney(amount);
		payment.setOrederId(orederId);
		payment.setPayCode(paymentKey);
		payment.setStatus(status);
		payment.setPayDate(LocalDateTime.now());
		pList.add(payment);
		paymentRepository.save(payment);
	}
	
	public void cancelInfo() {}
}
