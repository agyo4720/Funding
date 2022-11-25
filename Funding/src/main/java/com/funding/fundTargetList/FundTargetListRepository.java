package com.funding.fundTargetList;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundUser.FundUser;

public interface FundTargetListRepository extends JpaRepository<FundTargetList, Integer> {
	public List<FundTargetList> findByFundUser(FundUser funduser);
}
