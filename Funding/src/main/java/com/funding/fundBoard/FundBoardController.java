package com.funding.fundBoard;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
		
		return "/fundBoard/fundBoard_list";
	}
	
	@GetMapping("/create")
	public String create(FundBoardForm fundBoardForm) {
		return "/fundBoard/fundBoard_form";
	}
	
	@PostMapping("/create")
	public String create(
			@Valid FundBoardForm fundBoardForm,
			BindingResult bindingResult,
			Model model) {
		
		if(bindingResult.hasErrors()) {
			return "/fundBoard/fundBoard_form";
		}
		
		String time = fundBoardForm.getStartDate() + " " +fundBoardForm.getStartTime();
		
		this.fundBoardService.create(
				fundBoardForm.getSubject(),
				fundBoardForm.getContent(),
				fundBoardForm.getPlace(),
				time,
				fundBoardForm.getRuntime(),
				fundBoardForm.getFundDuration(),
				fundBoardForm.getMinFund(),
				fundBoardForm.getFundAmount());
		
		return "redirect:/fundBoard/list";
		
	}
	
	@RequestMapping("/detail/{id}")
	public String detail(@PathVariable ("id") Integer id, Model model) {
		
		FundBoard fundBoard = this.fundBoardService.findById(id);
		model.addAttribute("fundBoard", fundBoard);
		
		
		return "/fundBoard/fundBoard_detail";
	}
	
	
}
