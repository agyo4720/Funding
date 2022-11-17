package com.funding.Categorie;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategorieService {

	private final CategorieRepository categorieRepository;
	
	
	public List<Categorie> findAll(){
		List<Categorie> cList = categorieRepository.findAll();
		
		return cList;
	}
	
	public Categorie findById(Integer id) {
		Optional<Categorie> cate = categorieRepository.findById(id);
		
		return cate.get(); 
	}
	
	public void create(String name) {
		Categorie categorie = new Categorie();
		categorie.setCategoryName(name);
		
		categorieRepository.save(categorie);
	}
	
}
