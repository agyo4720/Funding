package com.funding.payment;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Service
public class PatmentService {
	private final SaleRepository saleRepository;
	private final PaymentRepository paymentRepository;
	
	public void savecreditinfo(String paymentKey, String orederId, int amount, String orderName) {
		CallbackPayload cp = new CallbackPayload();
		//cp.setCustomerName(customerName);
		cp.setOrderName(orderName);
		cp.setAmount(amount);
		cp.setOrderId(orederId);
		cp.setPaymentKey(paymentKey);
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!"+cp+"@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		
	}

	
}
