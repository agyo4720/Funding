package com.funding.Categorie;

<<<<<<< HEAD
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
=======
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
>>>>>>> namgo
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequestMapping("/categorie")
@RequiredArgsConstructor
@Controller
public class CategorieController {

	private final CategorieService categorieService;
	
<<<<<<< HEAD
	
	@GetMapping("/create")
	public String CategorieForm() {
		return "/categorie/categorieForm";
	}
	
	@PostMapping("/create")
	public String create(@RequestParam("categorieName")String name) {
		categorieService.create(name);
		return "redirect:/";
	}

=======
	// 카테고리 리스트
	@RequestMapping("/list")
	public String list(Model model) {
		
		List<Categorie> categorieList = this.CategorieService.findAll();
		model.addAttribute("categorieList", categorieList);
		
		return "/categorie/categorie_list";
	}
	
	// 카테고리 작성
	@GetMapping("/create")
	public String create(CategorieForm categorieForm) {
		return "/categorie/categorie_form";
	}
	
	// 카테고리 작성
	@PostMapping("/create")
	public String create(@Valid CategorieForm categorieForm, Model model) {
		
		this.CategorieService.create(categorieForm.getCategorieName());
		
		return "redirect:/categorie/list";
	}
	
	// 카테고리 삭제
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable ("id") Integer id) {
		this.CategorieService.delete(id);
		return "redirect:/categorie/list";
	}
	
	// 2022 - 11 -17 최종 작업본
	
	
	
	
>>>>>>> namgo
}
