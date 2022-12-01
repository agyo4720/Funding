package com.funding.user;

import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final FundUserService fundUserService;
	private final FundArtistService fundArtistService;
	private final FundTargetService fundTargetService;
	
	// nav에 사용자 이름 출력
	@RequestMapping("/navMyInfo")
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
	
	// 내 정보 페이지
	@RequestMapping("/myInfo")
	public String myInfo(HttpSession httpSession, Model model) {
		Object object = httpSession.getAttribute("myInfo");
		
		try {
			FundUser FU = (FundUser) object;
			model.addAttribute("myInfo",FU);
		}catch(Exception err) {
			FundArtist FA = (FundArtist) object;
			model.addAttribute("myInfo",FA);
		}
		
		return "user/myInfo";
	}
	
	// 내 정보 수정
	@RequestMapping("/modify/{username}")
	public String mofifyForm(@PathVariable("username") String username, Model model) {
		Optional<FundUser> FU = this.fundUserService.findByuserName(username);
		model.addAttribute("userData", FU.get());
		return "user/fundUserModifyForm";
	}
	
	
	
	
	
		
}
