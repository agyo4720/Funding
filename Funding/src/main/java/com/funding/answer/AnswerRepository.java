package com.funding.answer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundBoard.FundBoard;
import com.funding.fundBoardTarget.FundBoardTarget;

public interface AnswerRepository extends JpaRepository<Answer, Integer>{
	public List<Answer> findByFundBoardTarget(FundBoardTarget fundBoardTarget);
	
	public List<Answer> findByFundBoard(FundBoard fundBoard);
}
