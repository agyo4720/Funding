package com.funding.fundArtistList;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoard.FundBoardService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/fundArtistList")
public class FundArtistListController {
	
	private final FundArtistListService fundArtistListService;
	private final FundArtistService fundArtistService;
	private final FundBoardService fundBoardService;
	private final FundUserService fundUserService;
	
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
	
	// 펀드 참여 아티스트 투표하기
	@RequestMapping("/score/{id}")
	public String score(
			@PathVariable("id") Integer id,
			@PathVariable("fbid") Integer fbid,
			Principal principal,
			Model model) {

		// 로그인한 유저정보 == 투표한 유저 정보
		
		// 해당 아티스트의 id
		FundArtistList fal = this.fundArtistListService.findById(id);
		
		// 현재 유저 id에서 투표한 유저리스트
		Set<FundUser> sfu = fal.getFundUserList();
		
		// 지금 로그인한 유저정보
		Optional<FundUser> fu = this.fundUserService.findByuserName(principal.getName());
		
		sfu.contains(fu);
		
		if(sfu.contains(fu)) {
			return String.format("redirect:/fundBoard/detail/%s", fbid);
		}
		
		// 해당 펀드아티스트리스트 아이디
		FundArtistList fundArtistList = this.fundArtistListService.findById(id);
		
		// 투표하기한 유저정보
		FundUser fundUser = this.fundUserService.findByuserName(principal.getName()).get();
		
		this.fundArtistListService.addvote(
				fundArtistList.getFundBoard(),
				fundArtistList.getFundArtist(),
				fundUser
				,id);
		
		return "redirect:/fundBoard/list";
	}
	
	
	
	
}
