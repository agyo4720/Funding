package com.funding.fundTargetList;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundTargetListService {

	private final FundTargetListRepository fundTargetListRepository;
	private final FundUserService fundUserService;
	
	public void insertList(Principal principal, FundBoardTarget fundBoardTarget) {
		Optional<FundUser> user = fundUserService.findByuserName(principal.getName());
		
		FundTargetList fundTargetList = new FundTargetList();
		fundTargetList.setFundBoardTarget(fundBoardTarget);
		fundTargetList.setFundUser(user.get());
		
		fundTargetListRepository.save(fundTargetList);
	}
	
	
	public List<FundTargetList> findAll(FundUser user){
		List<FundTargetList> fList = fundTargetListRepository.findByFundUser(user);
		return fList;
	}
	
}
