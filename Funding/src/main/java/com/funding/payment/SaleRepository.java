package com.funding.payment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.Categorie.Categorie;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundUser.FundUser;

public interface SaleRepository extends JpaRepository<Sale,Integer> {
	List<Sale> findBypayCode(String nickname);
	List<Sale> findByFundBoardTarget(String fundBoardTarget);
	List<Sale> findByFundBoard(String fundBoard);
	List<Sale> findByid(Integer id);
	public Page<Sale> findByFundUser(String nickname,Pageable pageable);
}