package com.funding.fundTargetList;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundUser.FundUser;

public interface FundTargetListRepository extends JpaRepository<FundTargetList, Integer> {
	public List<FundTargetList> findByFundUser(FundUser funduser);
	public List<FundTargetList> findByFundBoardTarget(FundBoardTarget fundBoardTarget);
	public List<FundTargetList> findByFundUserAndFundBoardTarget(FundUser funduser, FundBoardTarget fundBoardTarget);
}
