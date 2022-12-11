package com.funding.fundTargetList;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service
public class FundTargetListService {

	private final FundTargetListRepository fundTargetListRepository;
	private final FundUserService fundUserService;
	
	//지정펀딩 펀딩하면 db에 등록
	public void insertList(Principal principal, FundBoardTarget fundBoardTarget) {
		Optional<FundUser> user = fundUserService.findByuserName(principal.getName());
		
		FundTargetList fundTargetList = new FundTargetList();
		fundTargetList.setFundBoardTarget(fundBoardTarget);
		fundTargetList.setFundUser(user.get());
		
		fundTargetListRepository.save(fundTargetList);
	}
	
	//유저로 펀드 리스트 부르기
	public List<FundTargetList> findByFundUser(FundUser user){
		List<FundTargetList> fList = fundTargetListRepository.findByFundUser(user);
		return fList;
	}
	
	//펀드글로 리스트 부르기
	public List<FundTargetList> findByFundBoardTarget(FundBoardTarget fundBoardTarget){
		List<FundTargetList> fList = fundTargetListRepository.findByFundBoardTarget(fundBoardTarget);
		return fList;
	}
	
	//해당 리스트 지우기
	public void delete(FundUser user, FundBoardTarget fundBoardTarget) {
		List<FundTargetList> ftList = fundTargetListRepository.findByFundUserAndFundBoardTarget(user, fundBoardTarget);
		fundTargetListRepository.delete(ftList.get(0));
	}
	
	public List<FundTargetList> findAll(){
		List<FundTargetList> targetList = fundTargetListRepository.findAll();
		return targetList;
	}
	
	
	
	
}
