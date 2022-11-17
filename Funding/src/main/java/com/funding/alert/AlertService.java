package com.funding.alert;

import org.springframework.stereotype.Service;

import com.funding.fundBoard.FundBoard;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AlertService {
	
	private final AlertRepository alertRepository;
	
	//댓글 생성시 알림 등록
	public void saveAnswer(FundBoard fundBoard, String content) {
		Alert alert = new Alert();
		alert.setContent(content);
		alert.setUrl(String.format("/fundBoard/%s", fundBoard.getId()));
		
		alertRepository.save(alert);
	}

}
