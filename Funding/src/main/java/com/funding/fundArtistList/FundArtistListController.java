package com.funding.fundArtistList;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoard.FundBoardService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/fundArtistList")
public class FundArtistListController {
	
	private final FundArtistListService fundArtistListService;
	private final FundArtistService fundArtistService;
	private final FundBoardService fundBoardService;
	
	// 펀드 아티스트 리스트 목록
	@RequestMapping("/list")
	public String list(Model model) {
		
		List<FundArtistList> FundArtistListList = this.fundArtistListService.findAll();
		model.addAttribute("FundArtistListList", FundArtistListList);
		
		return "fundArtistList_list";
	}
	
	// 펀드 아티스트 참여
	@RequestMapping("/join/{id}")
	public String join(
			@PathVariable("id") Integer id,
			Principal principal,
			Model model) {
		
		FundArtist fundArtist = this.fundArtistService.findByuserName(principal.getName()).get();
		FundBoard furndBoard = this.fundBoardService.findById(id);
		
		this.fundArtistListService.join(fundArtist, furndBoard);
		
		return "redirect:/fundBoard/list";
	}
	
	
	
	
}
