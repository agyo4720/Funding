package com.funding.Categorie;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequestMapping("/categorie")
@RequiredArgsConstructor
@Controller
public class CategorieController {

	private final CategorieService categorieService;
	
	
	
	@GetMapping("/create")
	public String CategorieForm() {
		return "/categorie/categorieForm";
	}
	
	@PostMapping("/create")
	public String create(@RequestParam("categorieName")String name) {
		categorieService.create(name);
		return "redirect:/";
	}
}
