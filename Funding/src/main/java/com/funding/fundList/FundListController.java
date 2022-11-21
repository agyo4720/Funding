package com.funding.fundList;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/fundList")
public class FundListController {
	
	private final FundListService fundListService;
	
	// 펀드 리스트 목록
	@RequestMapping("/list")
	public String list(Model model) {
		
		List<FundList> fundListList = this.fundListService.getFundList();
		model.addAttribute("fundListList", fundListList);
		
		return "fundList_list";
	}
	


}
