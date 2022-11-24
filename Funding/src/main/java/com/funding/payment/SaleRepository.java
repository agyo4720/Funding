package com.funding.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundUser.FundUser;

public interface SaleRepository extends JpaRepository<Sale,Integer> {
	List<Sale> findByFundUser(String nickname);
}