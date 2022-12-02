package com.funding.fundArtistList;

import java.security.Principal;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/fundArtistList")
public class FundArtistListController {
	
	private final FundArtistListService fundArtistListService;
	private final FundArtistService fundArtistService;
	
	// 펀드 아티스트 리스트 목록
	@RequestMapping("/list")
	public String list(Model model) {
		
		List<FundArtistList> FundArtistListList = this.fundArtistListService.findAll();
		model.addAttribute("FundArtistListList", FundArtistListList);
		
		return "fundArtistList_list";
	}
	
	// 펀드 아티스트 참여
	@RequestMapping("/join/{Username}")
	public String join(
			@PathVariable("Username") String Username,
			Principal principal,
			Model model) {
		
		
		
		FundArtist fundArtist = this.fundArtistService.findByuserName(Username).get();
		
		this.fundArtistListService.join(fundArtist);
		
		return "redirect:/fundBoard/list";
	}
	
	
	
	
}
