package com.funding.answerAns;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.funding.answer.Answer;
import com.funding.answer.AnswerService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AnswerAnsController {

	private final AnswerAnsService answerAnsService;
	private final AnswerService answerService;
	private final FundUserService fundUserService;
	
	//답변 생성
	@RequestMapping("a/create/{id}")
	public void createAnswerAns(@PathVariable("id")Integer id, @RequestParam("content")String content, Principal principal) {
		Answer answer = answerService.findById(id);
		FundUser fundUser = fundUserService.findByuserName(principal.getName());
		answerAnsService.createAnswerAns(content, answer, fundUser);
	}
	
	//답글 삭제
	@RequestMapping("a/delete/{id}")
	public void deleteAnswerAns(@PathVariable("id")Integer id) {
		AnswerAns answerAns = answerAnsService.findById(id);
		answerAnsService.deleteAnswerAns(answerAns);
	}
	
	
	
}
