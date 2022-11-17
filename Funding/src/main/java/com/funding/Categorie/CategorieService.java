package com.funding.Categorie;

import java.util.List;
<<<<<<< HEAD
=======
import java.util.Optional;
>>>>>>> df616c0b7e58bbef002feb1d258cf2f8bbea5d56

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
	
}
