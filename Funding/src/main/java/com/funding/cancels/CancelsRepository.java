package com.funding.cancels;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CancelsRepository extends JpaRepository<Cancels,Integer> {
	List<Cancels> findByFundUser(String nickname);
	List<Cancels> findByid(Integer id);
	public Page<Cancels> findByFundUser(String nickname,Pageable pageable);
}
