package com.funding.fundBoard;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FundBoardController {
	
	private final FundBoardRepository fundBoardRepository;
	private final FundBoardService fundBoardService; 
	
	// 펀딩 리스트
	@RequestMapping("fundBoard")
	public String getFundBoard(Model model) {
		List<FundBoard> fundBoard = this.fundBoardService.getFundBoard();
		model.addAttribute("fundBoard", fundBoard);
		return "fundBoard/fundBoard";
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
			 
			 List<FundBoard> fundBoardList = this.fundBoardRepository.findAll();
			 
			 model.addAttribute("fundBoardList", fundBoardList);
			 
			 return "fundBoard_form";
		 }
		
		
		 this.fundBoardService.create(fundBoardForm.getSubject(),
				 					  fundBoardForm.getContent(),
				 					  fundBoardForm.getPlace(),
				 					  fundBoardForm.getStartDate(),
				 					  fundBoardForm.getRuntime(),
				 					  fundBoardForm.getState(),
				 					  fundBoardForm.getFundDuration(),
				 					  fundBoardForm.getMinFund(),
				 					  fundBoardForm.getFundCurrent(),
				 					  fundBoardForm.getFundAmount(),
				 					  fundBoardForm.getCurrentMember(),
				 					  fundBoardForm.getVote(),
				 					  fundBoardForm.getStar());
		 
		 return "fundBoard";
	}
	
	
	
}
