package com.funding.fundBoard;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.Categorie.Categorie;

public interface FundBoardRepository extends JpaRepository<FundBoard, Integer>{

	public Page<FundBoard> findAll(Pageable pageable);

	public Page<FundBoard> findByCategorie(Pageable pageable, Categorie categorie);

	public List<FundBoard> findAll();
}
