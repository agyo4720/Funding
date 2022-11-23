package com.funding.fundBoard;

<<<<<<< HEAD
import java.awt.print.Pageable;

=======
>>>>>>> parent of 82cdd28 (2022/11/23 - 1 작업중)
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
import com.funding.Categorie.Categorie;


public interface FundBoardRepository extends JpaRepository<FundBoard, Integer>{
	
	public Page<FundBoard> findAll(Pageable pageable);
	
	public Page<FundBoard> findByCategorie(Pageable pageable, Categorie categorie);
	
=======
public interface FundBoardRepository extends JpaRepository<FundBoard, Integer>{
	
	Page<FundBoard> findAll(Pageable pageable);

>>>>>>> parent of 82cdd28 (2022/11/23 - 1 작업중)
}
