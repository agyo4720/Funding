package com.funding.answer;

import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.funding.alert.AlertService;
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
	private final AlertService alertService;
	
	
	//댓글 삭제
	@RequestMapping("/delete/{id}")
	public String deleteAnswer(@PathVariable("id")Integer id, @RequestParam("location")String where,
			@RequestParam("id")Integer bid) {
		answerService.deleteAnswer(id);
		if(where.equals("target")) {
			return String.format("redirect:/fundTarget/detail/%s", bid);
		}else {
			return String.format("redirect:/fundBoard/detail/%s", bid);
		}
	}
	
	
	//미지정 댓글 생성,id는 부모글 id
	@RequestMapping("/fundBoard/create/{id}")
	public String createBoardAnswer(@RequestParam("content")String content, @PathVariable("id")Integer id
			,Principal principal) {
		
		FundBoard fundBoard = fundBoardService.findById(id);
		Optional<FundUser> user = fundUserService.findByuserName(principal.getName());
		// 아티스트 일때  저장
		if(user.isEmpty()) {
			Optional<FundArtist> artiest = fundArtistService.findByuserName(principal.getName());
			answerService.createBoardAnswerArt(content, fundBoard, artiest.get());
			alertService.answerAlertBoard(fundBoard, principal.getName(), content);
			return String.format("redirect:/fundBoard/detail/%s", id);
		}
		
		alertService.answerAlertBoard(fundBoard, principal.getName(), content);
		answerService.createBoardAnswerUser(content, fundBoard, user.get());
		return String.format("redirect:/fundBoard/detail/%s", id);
		
	}
	
	//지정 댓글 생성,id는 부모글 id
	@RequestMapping("target/create/{id}")
	public String createTargetAnswer(@RequestParam("content")String content, @PathVariable("id")Integer id
			,Principal principal) {
		
		FundBoardTarget fundBoardTarget = fundTargetService.findById(id);
		
		Optional<FundUser> user = fundUserService.findByuserName(principal.getName());
		// 아티스트 일때  저장
		if(user.isEmpty()) {
			Optional<FundArtist> artiest = fundArtistService.findByuserName(principal.getName());
			answerService.createTargetAnswerArt(content, fundBoardTarget, artiest.get());
			alertService.answerAlertTarget(fundBoardTarget, principal.getName(), content);
			return String.format("redirect:/fundTarget/detail/%s", id);
		}
		// 유저 일 때
		alertService.answerAlertTarget(fundBoardTarget, principal.getName(), content);
		answerService.createTargetAnswerUser(content, fundBoardTarget, user.get());
		return String.format("redirect:/fundTarget/detail/%s", id);
	}
	
	
}
