package com.funding.fundList;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundBoard.FundBoard;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundListService {

	private final FundListRepository fundListRepository;
	private final FundUserService fundUserService;
	
	public List<FundList> getFundList(){
		return this.fundListRepository.findAll();
	}
	
	//결제시 목록 등록
	public void insertfund(Principal principal, FundBoard fundBoard) {
		Optional<FundUser> user = fundUserService.findByuserName(principal.getName());
		
		FundList fundList = new FundList();
		fundList.setFundBoard(fundBoard);
		fundList.setFundUser(user.get());
		
		fundListRepository.save(fundList);
	}
	
	//환불시 목록 삭제
	public void deleteFund(FundUser user, FundBoard fundBoard) {
		List<FundList> fList = fundListRepository.findByFundBoardAndFundUser(fundBoard, user);
		fundListRepository.delete(fList.get(0));
	}
	
	public List<FundList> findByFundUser(FundUser user){
		List<FundList> fList = fundListRepository.findByFundUser(user);
		return fList;
	}
	
	
	
	
	
}
