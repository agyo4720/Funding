package com.funding.fundList;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundBoard.FundBoard;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundTargetList.FundTargetList;
import com.funding.fundUser.FundUser;

public interface FundListRepository extends JpaRepository<FundList, Integer>{
	public List<FundList> findByFundBoard(FundBoard fundBoard);
	public List<FundList> findByFundUserAndFundBoard(FundUser funduser, FundBoard fundBoard);
}
