package com.funding.fundBoard;


import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.funding.Categorie.Categorie;
import com.funding.Categorie.CategorieService;
import com.funding.answer.Answer;
import com.funding.answer.AnswerService;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/fundBoard")
public class FundBoardController {
	
	private final FundBoardService fundBoardService;
	private final FundUserService fundUserService;
	private final CategorieService categorieService;
	private final FundArtistService fundArtistService;
	private final AnswerService answerService;
	
	// 미지정 펀드 리스트
	@RequestMapping("/list")
	public String list(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			Model model) {
		
		Page<FundBoard> fundBoardList = this.fundBoardService.findAll(page);
		model.addAttribute("fundBoardList", fundBoardList);
		
		return "fundBoard/fundBoard_list";
	}
	
	@GetMapping("/create")
	public String create(FundBoardForm fundBoardForm, Model model) {
		
		List<Categorie> categorieList = this.categorieService.findAll();
		model.addAttribute("categorieList", categorieList);
		
		return "/fundBoard/fundBoard_form";
	}
	
	@PostMapping("/create")
	public String create(
			@Valid FundBoardForm fundBoardForm,
			BindingResult bindingResult,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			
			List<Categorie> categorieList = this.categorieService.findAll();
			model.addAttribute("categorieList", categorieList);
			
			return "/fundBoard/fundBoard_form";
		}
		
		String time = fundBoardForm.getStartDate() + " " + fundBoardForm.getStartTime();
		
		
		this.fundBoardService.create(
				fundBoardForm.getCategorieName(),
				fundBoardForm.getSubject(),
				fundBoardForm.getContent(),
				fundBoardForm.getPlace(),
				time,
				fundBoardForm.getRuntime(),
				fundBoardForm.getFundDuration(),
				fundBoardForm.getMinFund(),
				fundBoardForm.getFundAmount(),
				fundBoardForm.getCreateDate()
				);
		
		return "redirect:/fundBoard/list";
		
	}
	
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable ("id") Integer id, Model model) {
		
		FundBoard fundBoard = this.fundBoardService.findById(id);
		model.addAttribute("fundBoard", fundBoard);
		
		List<Answer> answerList = this.answerService.findByFundBoard(fundBoard);
		model.addAttribute("answerList", answerList);
		
		return "/fundBoard/fundBoard_detail";
	}
	

//	// id값으로 카테고리 정열
//	@RequestMapping("/categorie/{id}")
//	public String categorie(
//			@RequestParam(value = "page", defaultValue = "0") Integer page,
//			@PathVariable("id") Integer id,
//			Model model) {
//		
//		List<Categorie> categorieList = this.categorieService.findAll();
//		model.addAttribute("categorieList", categorieList);
//		
//		Categorie categorie = this.categorieService.findById(id);
//		
//		Page<FundBoard> fundBoardList = this.fundBoardService.findByCategorieId(page, categorie);
//		model.addAttribute("fundBoardList", fundBoardList);
//		
//		return "/fundBoard/fundBoard_list";
//	}
	
	 // 2022/11/23 - 2 작업중
	
}
