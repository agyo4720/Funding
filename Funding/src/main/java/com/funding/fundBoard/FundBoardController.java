package com.funding.fundBoard;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.funding.Categorie.CategorieService;
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
	
	// 미지정 펀드 리스트
	@RequestMapping("/list")
	public String list(Model model) {
		
		List<FundBoard> fundBoardList = this.fundBoardService.getFundBoard();
		model.addAttribute("fundBoardList", fundBoardList);
		
		return "fundBoard_list";
	}
	
	@GetMapping("/create")
	public String create(FundBoardForm fundBoardForm) {
		return "fundBoard_form";
	}
	
	@PostMapping("/create")
	public String create(
			@Valid FundBoardForm fundBoardForm,
			BindingResult bindingResult,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			return "fundBoard_form";
		}
		
		this.fundBoardService.create(
				fundBoardForm.getSubject(),
				fundBoardForm.getContent(),
				fundBoardForm.getPlace(),
				fundBoardForm.getStartDate(),
				fundBoardForm.getRuntime(),
				fundBoardForm.getFundDuration(),
				fundBoardForm.getMinFund(),
				fundBoardForm.getFundAmount());
		
		return "fundBoard_form";
		
	}
	
	// 남규
	
}
