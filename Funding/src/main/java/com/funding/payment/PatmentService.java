package com.funding.payment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundUser.FundUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PatmentService {
	private final SaleRepository saleRepository;
	private final CancelsRepository cancelsRepository;
	private final EnrollRepository enrollRepository;
	private final RemitRepository remitRepository;
	
	
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
		log.info("sList: "+sList);
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
			Optional<FundUser> FU, String paymentKey) {
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
		
		List<Sale> sList = saleRepository.findBypayCode(paymentKey);		
		sList.get(0).setCheckin("환불");
		saleRepository.saveAll(sList);
	}
	
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
	
	//송금한내역
	public void remitInfo(String subMallId, Integer payoutAmount, String payoutDate) {
		List<Remit> rList = new ArrayList<>(); //서브몰등록 리스트
		Remit remit = new Remit();
		remit.setSubMallId(subMallId);
		remit.setPayoutAmount(payoutAmount);
		remit.setRequestedAt(payoutDate);
		remit.setPayoutDate(LocalDateTime.now());
		rList.add(remit);
		remitRepository.save(remit);
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
	
	
	//결제리스트 페이징
	public Page<Sale> findByFundUser(int page,String user){
		Pageable pageable = PageRequest.of(page, 5, Sort.by("payDate").descending());
		Page<Sale> sList = saleRepository.findByFundUser(user,pageable);
		return sList;
	}
	//환불리스트 페이징
	public Page<Cancels> findByCan(int pagee,String user){
		Pageable pageable = PageRequest.of(pagee, 5, Sort.by("canceledAt").descending());
		Page<Cancels> cList = cancelsRepository.findByFundUser(user,pageable);
		return cList;
	}
	
	//송금리스트 페이징
	public Page<Remit> findBysubMallId(int page,String subMallId){
		Pageable pageable = PageRequest.of(page, 5, Sort.by("payoutDate").descending());
		Page<Remit> rList = remitRepository.findBysubMallId(subMallId,pageable);
		return rList;
	}
}
