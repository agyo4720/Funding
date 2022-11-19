package com.funding.Categorie;

import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategorieService {

	private final CategorieRepository categorieRepository;
	
	// id로 카테고리 찾기
	public Categorie findById(Integer id) {
		
		Optional<Categorie> categorie = categorieRepository.findById(id);
		
		return categorie.get();
	}

	// 카테고리 리스트
	public List<Categorie> getCategorie(){
		return this.categorieRepository.findAll();
	}
	
	// 카테고리 목록
	public List<Categorie> findAll(){
		return this.categorieRepository.findAll();
	}
	
	// 카테고리 등록
	public void create(String categorieName) {
		
		Categorie categorie = new Categorie();
		
		categorie.setCategorieName(categorieName);
		
		this.categorieRepository.save(categorie);
	}
	
	// 카테고리 삭제
	public void delete(Integer id) {
		Optional<Categorie> categorie = this.categorieRepository.findById(id);
		this.categorieRepository.delete(categorie.get());
	}
	
	
}
