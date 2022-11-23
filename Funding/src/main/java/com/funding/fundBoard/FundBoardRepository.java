package com.funding.fundBoard;

import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
public interface FundBoardRepository extends JpaRepository<FundBoard,Integer>{
=======
import com.funding.Categorie.Categorie;

public interface FundBoardRepository extends JpaRepository<FundBoard, Integer>{
	
	public Page<FundBoard> findAll(Pageable pageable);
	
	public Page<FundBoard> findByCategorie(Pageable pageable, Categorie categorie);
	
>>>>>>> d0c9576d4cdd982911862a357dad6c3f46e223b3

}
