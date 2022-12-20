package com.funding.fundArtistList;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoard.FundBoardService;
import com.funding.fundList.FundList;
import com.funding.fundList.FundListService;
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
	private final FundListService fundListService;
	
	// 펀드 아티스트 리스트 목록
	@RequestMapping("/list")
	public String list(Model model) {
		
		List<FundArtistList> FundArtistListList = this.fundArtistListService.findAll();
		model.addAttribute("FundArtistListList", FundArtistListList);
		
		return "fundArtistList_list";
	}
	
	
	// 미지정 펀드 아티스트 참여
	@RequestMapping("/join/{id}")
	public String join(
			@PathVariable("id") Integer id,
			Principal principal,
			Model model) {
			
		FundArtist fundArtist = this.fundArtistService.findByuserName(principal.getName()).get();
		FundBoard furndBoard = this.fundBoardService.findById(id);
		
		Optional<FundArtistList> fal = this.fundArtistListService.search(fundArtist, furndBoard);
		
		if(fal.isEmpty()) {
			if(fundArtist.getSelfBoard() == null) {
				return String.format("redirect:/selfBoard/detail/%s", fundArtist.getUsername());
			}else {
				model.addAttribute("info", "success");
				this.fundArtistListService.join(fundArtist, furndBoard);
				return "/fundBoard/joinArtist";
			}
		}else{
			model.addAttribute("info", "fail");
			return "/fundBoard/joinArtist";
		}
	}
	
	// 펀드 참여 아티스트 투표하기
	@RequestMapping("/score/{faid}/{fbid}")
	public String score(
			@PathVariable("faid") Integer faid,
			@PathVariable("fbid") Integer fbid,
			Principal principal,
			Model model,
			RedirectAttributes rttr) {

		
		// 해당 펀드아티스트리스트 아이디
		FundArtistList fundArtistList = this.fundArtistListService.findById(faid);
		FundBoard fundBoard = this.fundBoardService.findById(fbid);
		// 투표하기한 유저정보
		FundUser fundUser = this.fundUserService.findByuserName(principal.getName()).get();
		
		List<FundList> fundList = fundListService.findByFundUserAndFundBoard(fundUser, fundBoard);
		
		if(fundList.size() <= 0) {
			rttr.addFlashAttribute("vote", "false");
			return String.format("redirect:/fundBoard/detail/%s", fbid);
		}else {
			this.fundArtistListService.addvote(
					fundArtistList.getFundBoard(),
					fundArtistList.getFundArtist(),
					fundUser,
					faid);
		}
		
		
		
		
		return String.format("redirect:/fundBoard/detail/%s", fbid);
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
	
	//아티스트 신청 취소
	@RequestMapping("/delete/{id}")
	public String cancelArtist(@PathVariable("id")Integer id) {
		FundArtistList fundArtistList = fundArtistListService.findById(id);
		fundArtistListService.delete(fundArtistList);
		return "redirect:/";
	}
	
}
