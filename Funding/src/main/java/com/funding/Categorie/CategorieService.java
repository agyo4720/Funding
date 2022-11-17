package com.funding.Categorie;

import java.util.List;
<<<<<<< HEAD
import java.util.Optional;
=======
>>>>>>> namgo

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategorieService {

	private final CategorieRepository categorieRepository;
	
<<<<<<< HEAD
	
	public List<Categorie> findAll(){
		List<Categorie> cList = categorieRepository.findAll();
		
		return cList;
	}
	
	public Categorie findById(Integer id) {
		Optional<Categorie> cate = categorieRepository.findById(id);
		
		return cate.get(); 
=======
	// 카테고리 리스트
	public List<Categorie> getCategorie(){
		return this.categorieRepository.findAll();
>>>>>>> namgo
	}
	
}
