package com.funding.Categorie;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CategorieRepository extends JpaRepository<Categorie, Integer>{
	
	// 카테고리 이름 조회
	Optional<Categorie> findByCategorieName(String categorieName);
	
}
