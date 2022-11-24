package com.funding.alert;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundUser.FundUser;

public interface AlertRepository extends JpaRepository<Alert, Integer>{
	public List<Alert> findByHostUser(FundUser user);
}
