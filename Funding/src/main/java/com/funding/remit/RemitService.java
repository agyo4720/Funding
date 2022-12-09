package com.funding.remit;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class RemitService {
	
	private final RemitRepository remitRepository;

	//송금한내역
	public void remitInfo(String subMallId, Integer payoutAmount, String payoutDate) {
		List<Remit> rList = new ArrayList<>(); //서브몰등록 리스트
		Remit remit = new Remit();
		remit.setSubMallId(subMallId);//고유ID
		remit.setPayoutAmount(payoutAmount);//송금금액
		remit.setPayoutDate(payoutDate);//공연완료일
		remit.setRequestedAt(LocalDateTime.now());//송금등록일
		rList.add(remit);
		remitRepository.save(remit);
	}

	//송금리스트 페이징
	public Page<Remit> findBysubMallId(int page,String subMallId){
		Pageable pageable = PageRequest.of(page, 5, Sort.by("requestedAt").descending());//송금등록일 기준으로 정렬
		Page<Remit> rList = remitRepository.findBysubMallId(subMallId,pageable);// 페이징
		return rList;
	}
}
