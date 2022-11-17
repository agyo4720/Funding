package com.funding.Categorie;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategorieService {

	private final CategorieRepository categorieRepository;
	
	// 카테고리 리스트
	public List<Categorie> getCategorie(){
		return this.categorieRepository.findAll();
	}
	
}
