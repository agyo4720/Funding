package com.funding.fundArtistList;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoard.FundBoardService;
import com.funding.fundTargetList.FundTargetList;
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
	@RequestMapping("/score/{id}/{bid}")
	public String score(
			@PathVariable("id") Integer id,
			@PathVariable("bid") Integer bid,
			Principal principal,
			Model model) {

		// 로그인한 유저정보 == 투표한 유저 정보
		FundArtistList fundAl = this.fundArtistListService.findById(id);
		
		Set<FundUser> sfu = fundAl.getFundUserList();
		
		Optional<FundUser> fu = this.fundUserService.findByuserName(principal.getName());
		
		sfu.contains(fu);
		
		if(sfu.contains(fu)) {
			
			model.addAttribute("msg", "ture");
			
			return String.format("redirect:/fundBoard/detail/%s", bid);
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
	
	
	
	
	//미지정펀딩 참여한 목록 불러오기(ajax)
	@RequestMapping("/show")
	@ResponseBody
	public List<HashMap<String, String>> currentFundList(@RequestParam("artist")String username) {
		
		Optional<FundArtist> artist = fundArtistService.findByuserName(username);
		
		
		//현재 펀딩목록 추가
		List<FundArtistList> faList = fundArtistListService.findByFundArtist(artist.get());
		List<HashMap<String, String>> fundList = new ArrayList<>();
		for(int i=0; i<faList.size(); i++) {
			HashMap<String, String> fmap = new HashMap<String, String>();
			
			
			fmap.put("fundName", faList.get(i).getFundBoard().getSubject());
			fmap.put("status", faList.get(i).getFundBoard().getState());
			fmap.put("url", "/fundBoard/detail/" + faList.get(i).getFundBoard().getId());
			double now = faList.get(i).getFundBoard().getFundCurrent();
			double max = faList.get(i).getFundBoard().getFundAmount();
			double percent = now / max * 100.0;
			fmap.put("percent", String.format("%.2f",percent) + "%");
			
			fundList.add(fmap);
		}
		
		return fundList;
	}
	
}
