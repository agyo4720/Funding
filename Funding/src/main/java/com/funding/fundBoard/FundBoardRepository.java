package com.funding.fundBoard;

<<<<<<< HEAD
<<<<<<< HEAD

import java.awt.print.Pageable;

=======
>>>>>>> parent of 82cdd28 (2022/11/23 - 1 작업중)
=======
>>>>>>> parent of 82cdd28 (2022/11/23 - 1 작업중)
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

<<<<<<< HEAD
<<<<<<< HEAD

import com.funding.Categorie.Categorie;


=======
>>>>>>> parent of 82cdd28 (2022/11/23 - 1 작업중)
=======
>>>>>>> parent of 82cdd28 (2022/11/23 - 1 작업중)
public interface FundBoardRepository extends JpaRepository<FundBoard, Integer>{
	
	Page<FundBoard> findAll(Pageable pageable);

}
