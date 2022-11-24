package com.funding;

import java.security.Principal;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	
	private final FundUserService fundUserService;
	private final FundArtistService fundArtistService;
	
	// 메인페이지 요청시 로그인된 사용자 정보를 같이 넘겨준다
	@RequestMapping("/")
	public String home(Model model, Principal principal) {
		
//	 Optional<FundUser> fundUser =  this.fundUserService.findByuserName(principal.getName());
//	 Optional<FundArtist> fundArtist = this.fundArtistService.findByuserName(principal.getName());
//	 
//	 if(fundUser.isPresent()) {
//		 model.addAttribute("userData",fundUser);
//	 }
//	 
//	 if(fundArtist.isPresent()) {
//		 model.addAttribute("userData",fundArtist);
//	 }
	 
	 
		return "main/home";
	}
}
