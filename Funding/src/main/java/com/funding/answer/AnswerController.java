package com.funding.answer;

import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoard.FundBoardService;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/answer")
@RequiredArgsConstructor
@Controller
public class AnswerController {

	private final AnswerService answerService;
	private final FundBoardService fundBoardService;
	private final FundTargetService fundTargetService;
	private final FundUserService fundUserService;
	private final FundArtistService fundArtistService;
	
	
	//댓글 삭제
	@RequestMapping("/delete/{id}")
	public String deleteAnswer(@PathVariable("id")Integer id, @RequestParam("location")String where,
			@RequestParam("id")Integer bid) {
		answerService.deleteAnswer(id);
		if(where.equals("target")) {
			return String.format("redirect:/fundTarget/detail/%s", bid);
		}
		return "redirect:/";
	}
	
	
	//댓글 생성,id는 부모글 id
	@RequestMapping("board/create/{id}")
	public void createBoardAnswer(@RequestParam("content")String content, @PathVariable("id")Integer id) {
		FundBoard fundBoard = fundBoardService.findById(id);
		answerService.createBoardAnswer(content, fundBoard);
		
	}
	
	//댓글 생성,id는 부모글 id
	@RequestMapping("target/create/{id}")
	public String createTargetAnswer(@RequestParam("content")String content, @PathVariable("id")Integer id
			,Principal principal) {
		
		FundBoardTarget fundBoardTarget = fundTargetService.findById(id);
		
		Optional<FundUser> user = fundUserService.findByuserName(principal.getName());
		
		if(user.isEmpty()) {
			Optional<FundArtist> artiest = fundArtistService.findByuserName(principal.getName());
			answerService.createTargetAnswerArt(content, fundBoardTarget, artiest.get());
			return String.format("redirect:/fundTarget/detail/%s", id);
		}
		
		
		answerService.createTargetAnswerUser(content, fundBoardTarget, user.get());
		return String.format("redirect:/fundTarget/detail/%s", id);
	}
	
	@RequestMapping("/fundBoard/create/{id}")
	public String createFundBoardAnswer(
			@RequestParam("content")String content,
			@PathVariable("id")Integer id) {
		
		FundBoard fundBoard = this.fundBoardService.findById(id);
		this.answerService.createBoardAnswer(content, fundBoard);
		
		return String.format("redirect:/fundBoard/detail/%s", id);
		
	}
	
}
