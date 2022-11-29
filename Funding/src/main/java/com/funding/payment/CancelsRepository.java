package com.funding.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface CancelsRepository extends JpaRepository<Cancels,Integer> {
	List<Cancels> findByFundUser(String nickname);
}
