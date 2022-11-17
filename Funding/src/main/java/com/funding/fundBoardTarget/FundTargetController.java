package com.funding.fundBoardTarget;

import java.util.List;

import javax.validation.Valid;

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

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/fundTarget")
@RequiredArgsConstructor
@Controller
public class FundTargetController {

	private final FundTargetService fundTargetService;
	private final CategorieService categorieService;
	private final AnswerService answerService;
	
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
	
	//글 목록 보기
	@RequestMapping("")
	public String showList(Model model) {
		List<FundBoardTarget> targetList = fundTargetService.findAll();
		
		model.addAttribute("targetList", targetList);
		return "fundTarget/fundTargetList";
	}
	
	//디테일 창으로
	@RequestMapping("/detail/{id}")
	public String showDetail(Model model, @PathVariable("id")Integer id) {
		FundBoardTarget fundBoardTarget = fundTargetService.findById(id);
		List<Answer> aList = answerService.findByFundBoardTarget(fundBoardTarget);
		
		model.addAttribute("aList", aList);
		model.addAttribute("fundBoardTarget", fundBoardTarget);
		return "/fundTarget/fundTargetDetail";
	}
	
}
