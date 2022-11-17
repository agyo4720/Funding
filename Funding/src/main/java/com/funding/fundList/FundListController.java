package com.funding.fundList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FundListController {
	
	private final FundListService fundListService;

	@RequestMapping("fundList")
	public String fundList() {
		
		return "fundList";
	}
}
