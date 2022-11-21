package com.funding.payment;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CallbackPayload {
	private String paymentKey; //결제완료키
	private String transactionKey; //거래조회키
    
    private String orderId; //주문번호
    private String orderName; // 공연이름
    private int amount; //금액
    private String customerName; //고객이름
    private String cancelReason; //취소사유
}
