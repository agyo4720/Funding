package com.funding.cancels;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CancelsRepository extends JpaRepository<Cancels,Integer> {
	List<Cancels> findByFundUser(String nickname); //고객이름 리스트
	List<Cancels> findByid(Integer id); //환불 고유번호 리스트
}
