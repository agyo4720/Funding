package com.funding.fundBoard;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FundBoardRepository extends JpaRepository<FundBoard, Integer>{
	
	Page<FundBoard> findAll(Pageable pageable);

}
