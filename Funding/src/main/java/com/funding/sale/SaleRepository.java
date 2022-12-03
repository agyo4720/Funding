package com.funding.sale;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.Categorie.Categorie;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundUser.FundUser;

public interface SaleRepository extends JpaRepository<Sale,Integer> {
	List<Sale> findBypayCode(String payCode);//결제키
	List<Sale> findByOrederId(String orederId);//주문번호
	List<Sale> findByFundBoardTarget(String fundBoardTarget);//지정공연이름
	List<Sale> findByFundBoard(String fundBoard);//미지정공연이름
	List<Sale> findByid(Integer id);//기본키ID
	public Page<Sale> findByUsername(String username,Pageable pageable);//페이징
}