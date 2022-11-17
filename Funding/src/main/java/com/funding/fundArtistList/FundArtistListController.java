package com.funding.fundArtistList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class FundArtistListController {
	
	private final FundArtistListService fundArtistListService;
	
	@RequestMapping("/fundArtistList")
	public String fundArtistList() {
		return "fundArtistList";
	}

}
