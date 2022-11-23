package com.funding.fundBoard;

import java.awt.print.Pageable;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundBoardRepository extends JpaRepository<FundBoard, Integer>{
	
	Page<FundBoard> findAll(Pageable pageable);

}
