package com.funding;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final FundUserService fundUserService;
	private final FundArtistService fundArtistService;
	private final FundTargetService fundTargetService;
	
	// 메인페이지 요청시 로그인된 사용자 정보를 같이 넘겨준다
	@RequestMapping("/")
	public String home(Model model, Principal principal) {
		
	
		if(principal != null) {
			Optional<FundUser> fundUser =  this.fundUserService.findByuserName(principal.getName());
			Optional<FundArtist> fundArtist = this.fundArtistService.findByuserName(principal.getName());
			 
			if(fundUser.isPresent()) {
				model.addAttribute("userData",fundUser.get());
			}
		 
			if(fundArtist.isPresent()) {
				model.addAttribute("userData",fundArtist.get());
			}
		}
		Page<FundBoardTarget> page = fundTargetService.findAll(0); 
		
		model.addAttribute("page", page);
		return "main/home";
	}
	
	
	// nav에 사용자 이름 출력
	@RequestMapping("/user/navMyInfo")
	@ResponseBody
	public HashMap<String, Object> navMyInfo(String username) {
		Optional<FundUser> FU = fundUserService.findByuserName(username);
		Optional<FundArtist> FA = fundArtistService.findByuserName(username);
		HashMap<String, Object> user = new HashMap<>();
		
		if(FU.isPresent()) {
			user.put("userName", FU.get().getNickname());
			user.put("userRole", FU.get().getRole());
			
			return user;
		}else if(FA.isPresent()) {
			user.put("userName", FA.get().getNickname());
			user.put("userRole", FA.get().getRole());
			
			return user;
		}
		
		return user;
	}
	
}
