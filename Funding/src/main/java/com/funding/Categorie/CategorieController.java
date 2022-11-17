package com.funding.Categorie;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/categorie")
public class CategorieController {

	private final CategorieService CategorieService;
	
	// 카테고리 리스트
	@RequestMapping("/list")
	public String list(Model model) {
		
		List<Categorie> categorieList = this.CategorieService.getCategorie();
		model.addAttribute("categorieList", categorieList);
		
		return "/categorie_list";
	}
	
}
