package com.funding.fundBoardTarget;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.Categorie.Categorie;

public interface FundTargetRepository extends JpaRepository<FundBoardTarget, Integer>{
	public Page<FundBoardTarget> findAll(Pageable pageable);
	public Page<FundBoardTarget> findByCategorie(Categorie categorie, Pageable pageable);
	public List<FundBoardTarget> findAll();
}
