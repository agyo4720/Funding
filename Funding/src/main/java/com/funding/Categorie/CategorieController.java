package com.funding.Categorie;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CategorieController {

	private final CategorieService CategorieService;
	
	
	
	@RequestMapping("/Categorie")
	public String Categorie() {
		return "Categorie";
	}
}
