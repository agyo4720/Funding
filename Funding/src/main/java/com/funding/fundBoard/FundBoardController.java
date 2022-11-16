package com.funding.fundBoard;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.funding.Categorie.CategorieService;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FundBoardController {
	
	private final FundBoardRepository fundBoardRepository;
	private final FundBoardService fundBoardService;
	private final FundUserService fundUserService;
	private final CategorieService categorieService;
	private final FundArtistService fundArtistService;
	
	
	// 펀딩 리스트
	@RequestMapping("fundBoard")
	public String getFundBoard(Model model) {
		List<FundBoard> fundBoard = this.fundBoardService.getFundBoard();
		model.addAttribute("fundBoard", fundBoard);
		return "fundBoard";
	}
	
//	// 펀딩리스트
//	@RequestMapping("/fundBoard/create")
//	public String fundBoardList(Model model) {
//		
//		List<FundBoard> fundBoardList = this.fundBoardService.getFundBoard();
//
//		model.addAttribute("fundBoard", fundBoardList);
//		
//		return "fundBoard";
//	}
	
	// 펀딩작성
	@PostMapping("/fundBoard/create")
	public String fundBoardCreate(@Valid FundBoardForm fundBoardForm,
								  BindingResult bindingResult,
								  Principal principal,
								  Model model) {
		
		 if (bindingResult.hasErrors()) {
			 
			 //List<FundBoard> fundBoardList = this.fundBoardRepository.findAll();
			 this.fundBoardService.getFundBoard();
			 
			 model.addAttribute("fundBoardList", this.fundBoardService.getFundBoard());
			 
			 return "fundBoard_form";
		 }
		
		
//		 this.fundBoardService.create(fundBoardForm.getSubject(),
//				 					  fundBoardForm.getContent(),
//				 					  fundBoardForm.getPlace(),
//				 					  fundBoardForm.getStartDate(),
//				 					  fundBoardForm.getRuntime(),
//				 					  fundBoardForm.getFundDuration(),
//				 					  fundBoardForm.getMinFund(),
//				 					  fundBoardForm.getFundAmount(),
//				 					  fundBoardForm.getCurrentMember());
		 
		 return "fundBoard";
	}
	
	
	
}
