package com.funding.fundList;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundBoard.FundBoard;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundTargetList.FundTargetList;
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
	
	
	//지정펀딩 펀딩하면 db에 등록
	public void insertList(Principal principal, FundBoard fundBoard) {
		Optional<FundUser> user = fundUserService.findByuserName(principal.getName());
		
		FundList fundBoardList = new FundList();
		fundBoardList.setFundBoard(fundBoard);
		fundBoardList.setFundUser(user.get());
		
		fundListRepository.save(fundBoardList);
	}
	
	
	//펀드글로 리스트 부르기
	public List<FundList> findByFundBoard(FundBoard fundBoard){
		List<FundList> fList = fundListRepository.findByFundBoard(fundBoard);
		return fList;
	}
	
	//해당 리스트 지우기
	public void delete(FundUser user, FundBoard fundBoard) {
		List<FundList> fList = fundListRepository.findByFundUserAndFundBoard(user, fundBoard);
		fundListRepository.delete(fList.get(0));
	}
}
