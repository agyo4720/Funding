package com.funding.enroll;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class EnrollService {
	private final EnrollRepository enrollRepository;
	
	
	//서브몰ID 등록
	public void enrollInfo(String subMallId, String companyName, String representativeName, 
			String businessNumber, String bank, String accountNumber) {
		List<Enroll> eList = new ArrayList<>(); //서브몰등록 리스트
		Enroll enroll = new Enroll();
		enroll.setSubMallId(subMallId);
		enroll.setCompanyName(companyName);
		enroll.setRepresentativeName(representativeName);
		enroll.setBusinessNumber(businessNumber);
		enroll.setBank(bank);
		enroll.setAccountNumber(accountNumber);
		eList.add(enroll);
		log.info("eList: "+eList);
		enrollRepository.save(enroll);
	}
	
	//서브몰ID 수정
	public void reviseInfo(String subMallId, String companyName, String representativeName, 
			String businessNumber, String bank, String accountNumber) {
		List<Enroll> eList = enrollRepository.findBysubMallId(subMallId);
		log.info("eList: "+eList);
		eList.get(0).setSubMallId(subMallId);
		eList.get(0).setCompanyName(companyName);
		eList.get(0).setRepresentativeName(representativeName);
		eList.get(0).setBusinessNumber(businessNumber);
		eList.get(0).setBank(bank);
		eList.get(0).setAccountNumber(accountNumber);
		enrollRepository.saveAll(eList);
	}
	
	//서브몰ID 삭제
	public void deletionInfo(String subMallId) {
		List<Enroll> eList = enrollRepository.findBysubMallId(subMallId);
		log.info("eList: "+eList);
		eList.get(0).setSubMallId(subMallId);
		enrollRepository.deleteAll(eList);
	}	
}
