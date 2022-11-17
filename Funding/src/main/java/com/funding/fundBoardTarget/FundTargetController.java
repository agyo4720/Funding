package com.funding.fundBoardTarget;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.funding.Categorie.Categorie;
import com.funding.Categorie.CategorieService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/fundTarget")
@RequiredArgsConstructor
@Controller
public class FundTargetController {

	private final FundTargetService fundTargetService;
	private final CategorieService categorieService;
	
	//글 작성폼 불러오기
	@GetMapping("/form")
	private String form(TargetForm targetForm, Model model) {
		List<Categorie> cList = categorieService.findAll();
		model.addAttribute("cList",cList);
		return "/fundTarget/fundTargetForm";
	}
	
	
	//글 작성하기
	@PostMapping("/form")
	private String create(@Valid TargetForm targetForm,BindingResult bindingResult,
			@RequestParam("categorie")Integer cid) {
		
		String startTime = targetForm.getStartDate() + " " +  targetForm.getStartTime();
		Categorie categorie = categorieService.findById(cid);
		
		
		fundTargetService.create(
				targetForm.getSubject(), 
				targetForm.getContent(), 
				targetForm.getAertiest(),
				targetForm.getPlace(),
				targetForm.getRuntime(),
				targetForm.getFundDurationE(),
				startTime,
				targetForm.getMinFund(),
				targetForm.getFundAmount(),
				categorie
				);
		return "redirect:/";
	}
	
	
}
