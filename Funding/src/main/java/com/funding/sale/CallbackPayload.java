package com.funding.sale;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class CallbackPayload {
	private String fundUser; //고객이름
	private String orderName; // 공연이름
	private int amount; //금액
	private String orderId; //주문번호
	private String paymentKey; //결제완료키
	private String status; //상태
	
    private String cancelReason; //취소사유
}
