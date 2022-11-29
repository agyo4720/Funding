package com.funding.payment;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.Categorie.Categorie;
import com.funding.fundUser.FundUser;

public interface CancelsRepository extends JpaRepository<Cancels,Integer> {
	List<Cancels> findByFundUser(String nickname);
	//public Page<Cancels> findAll(Pageable pageable);
}
