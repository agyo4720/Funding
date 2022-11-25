package com.funding.fundBoard;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/fundBoard")
public class FundBoardController {
	
	private final FundBoardService fundBoardService;
	private final FundUserService fundUserService;
	private final CategorieService categorieService;
	private final FundArtistService fundArtistService;
	private final AnswerService answerService;

	
	// 미지정 펀드 리스트(페이징)
	@RequestMapping("/list")
	public String list(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			Model model) {
		
		Page<FundBoard> fundBoardList = this.fundBoardService.findAll(page);
		model.addAttribute("fundBoardList", fundBoardList);
		
		List<Categorie> categorieList = this.categorieService.findAll();
		model.addAttribute("categorieList", categorieList);
		
		return "fundBoard/fundBoard_list";
	}
	
	// 미지정 펀드 등록(GET)
	@GetMapping("/create")
	public String create(FundBoardForm fundBoardForm, Model model) {
		
		List<Categorie> categorieList = this.categorieService.findAll();
		model.addAttribute("categorieList", categorieList);
		
		return "/fundBoard/fundBoard_form";
	}
	
	// 미지정 펀드 등록(POST)
	@PostMapping("/create")
	public String create(
			@Valid FundBoardForm fundBoardForm,
			BindingResult bindingResult,
			Principal principal,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			
			List<Categorie> categorieList = this.categorieService.findAll();
			model.addAttribute("categorieList", categorieList);
			
			return "/fundBoard/fundBoard_form";
		}
		
		// 날짜 데이터와 시간 데이터를 합쳐서 데이터 넣기
		// String time = fundBoardForm.getStartDate() + " " + fundBoardForm.getStartTime();
		
		Optional<FundUser> fundUser = this.fundUserService.findByuserName(principal.getName());
		
		this.fundBoardService.create(
				fundBoardForm.getCategorieName(),
				fundBoardForm.getSubject(),
				fundBoardForm.getContent(),
				fundBoardForm.getPlace(),
				fundBoardForm.getStartDateTime(),
				fundBoardForm.getFundDuration(),
				fundBoardForm.getRuntime(),
				fundBoardForm.getMinFund(),
				fundBoardForm.getFundAmount(),
				fundBoardForm.getCreateDate(),
				fundUser.get()
				);
		
		return "redirect:/fundBoard/list";
		
	}
	
	// 미지정 펀드 답변등록
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable ("id") Integer id, Model model) {
		
		FundBoard fundBoard = this.fundBoardService.findById(id);
		model.addAttribute("fundBoard", fundBoard);
		
		List<Answer> answerList = this.answerService.findByFundBoard(fundBoard);
		model.addAttribute("answerList", answerList);
		
		return "/fundBoard/fundBoard_detail";
	}
	
	// id로 카테고리 리스트 가져오기
	@RequestMapping("/categorie/{id}")
	public String categorie(
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@PathVariable("id") Integer id,
			Model model) {
		
		Categorie categorie = this.categorieService.findById(id);
		
		Page<FundBoard> fundBoardList = this.fundBoardService.findByCategorie(page, categorie);
		model.addAttribute("fundBoardList", fundBoardList);
		
		List<Categorie> categorieList = this.categorieService.findAll();
		model.addAttribute("categorieList", categorieList);
		
		return "/fundBoard/fundBoard_list";
	}
	
	// 미지정 펀드 펀딩하기
	@RequestMapping("/request/{id}")
	public String request(
			@PathVariable("id") Integer id,
			@RequestParam("minFund") Integer minFund,
			@RequestParam("star") Integer star,
			Model model) {
		
		FundBoard fundBoard = this.fundBoardService.findById(id);
		this.fundBoardService.create(minFund, star);
		
		return String.format("redirect:/fundBoard/detail/%s", id);
	}
	
	// 2022/11/25 - 2 작업중

	
}
