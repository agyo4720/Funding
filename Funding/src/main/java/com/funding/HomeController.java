package com.funding;

import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.alert.AlertService;
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
	private final AlertService alertService;
	
	// 메인페이지 요청시 로그인된 사용자 정보를 같이 넘겨준다
	@RequestMapping("/")
	public String home(Model model, Principal principal, Integer alertId) {
		
	
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
		
		//알림삭제
		if(alertId != null) {
			alertService.deleteAlert(alertId);
		}
		
		model.addAttribute("page", page);
		return "main/home";
	}
	
	
	
}
