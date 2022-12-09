package com.funding.fundArtistList;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoard.FundBoardService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;
import com.funding.selfBoard.SelfBoardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/fundArtistList")
public class FundArtistListController {
	
	private final FundArtistListService fundArtistListService;
	private final FundArtistService fundArtistService;
	private final FundBoardService fundBoardService;
	private final FundUserService fundUserService;
	private final SelfBoardService selfBoardServie;
	
	// 펀드 아티스트 리스트 목록
	@RequestMapping("/list")
	public String list(Model model) {
		
		List<FundArtistList> FundArtistListList = this.fundArtistListService.findAll();
		model.addAttribute("FundArtistListList", FundArtistListList);
		
		return "fundArtistList_list";
	}
	
	// 미지정 펀드 아티스트 참여
	@RequestMapping("/join/{id}")
	@ResponseBody
	public String join(
			@PathVariable("id") Integer id,
			Principal principal,
			Model model) {
			
		FundArtist fundArtist = this.fundArtistService.findByuserName(principal.getName()).get();
		FundBoard furndBoard = this.fundBoardService.findById(id);
		
		Optional<FundArtistList> fal = this.fundArtistListService.search(fundArtist, furndBoard);
		
		fundArtist.getSelfBoard();
				
		if(fundArtist.getSelfBoard() == null) {
			return "프로필을 먼저 등록해주세요.";
		}
		
		if(fal.isPresent()) {
			return "이미 참여했습니다.";
		}
				
		this.fundArtistListService.join(fundArtist, furndBoard);

		return "참여 완료되었습니다.";
	}
	
	// 펀드 참여 아티스트 투표하기
	@RequestMapping("/score/{faid}/{fbid}")
	public String score(
			@PathVariable("faid") Integer faid,
			@PathVariable("fbid") Integer fbid,
			Principal principal,
			Model model) {

//		// 로그인한 유저정보 == 투표한 유저 정보
//		
//		// 해당 아티스트의 id
//		FundArtistList fal = this.fundArtistListService.findById(faid);
//				
//		// 현재 유저 id에서 투표한 유저리스트
//		Set<FundUser> sfu = fal.getFundUserList();
//		
//		// 지금 로그인한 유저정보
//		Optional<FundUser> fu = this.fundUserService.findByuserName(principal.getName());
//		
//		if(sfu.equals(fu)) {
//			return "redirect:/fundBoard/list";
//		}
		
		// 해당 펀드아티스트리스트 아이디
		FundArtistList fundArtistList = this.fundArtistListService.findById(faid);
		
		// 투표하기한 유저정보
		FundUser fundUser = this.fundUserService.findByuserName(principal.getName()).get();
			
		this.fundArtistListService.addvote(
				fundArtistList.getFundBoard(),
				fundArtistList.getFundArtist(),
				fundUser,
				faid);
		
		return String.format("redirect:/fundBoard/detail/%s", fbid);
	}
	

}
