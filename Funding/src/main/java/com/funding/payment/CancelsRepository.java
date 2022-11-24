package com.funding.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundUser.FundUser;

public interface CancelsRepository extends JpaRepository<Cancels,Integer> {
	List<Cancels> findByFundUser(FundUser user);
}
