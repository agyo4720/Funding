package com.funding.fundList;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundBoard.FundBoard;

import com.funding.fundUser.FundUser;

public interface FundListRepository extends JpaRepository<FundList, Integer>{
	List<FundList> findByFundBoardAndFundUser(FundBoard board, FundUser user);
	List<FundList> findByFundUser(FundUser user);
	public List<FundList> findByFundBoard(FundBoard fundBoard);
	List<FundList> findByFundUserAndFundBoard(FundUser user, FundBoard board);
}
