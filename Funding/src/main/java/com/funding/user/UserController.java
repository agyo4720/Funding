package com.funding.user;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;
import com.funding.user.mailValidation.EmailService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final FundUserService fundUserService;
	private final FundArtistService fundArtistService;
	private final FundTargetService fundTargetService;
	private final EmailService emailService;
	
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
	public String myInfo(Principal principal, Model model) {
		Optional<FundUser> FU = this.fundUserService.findByuserName(principal.getName());
		Optional<FundArtist> FA = this.fundArtistService.findByuserName(principal.getName());
		if(FU.isPresent()) {
			model.addAttribute("myInfo",FU.get());
		} else if(FA.isPresent()) {
			model.addAttribute("myInfo",FA.get());
		}
		
		return "user/myInfo";
	}
	
	// 비밀번호 초기화 폼 요청
	@GetMapping("/resetPwd")
	public String resetPwd() {
		
		return "/user/resetPwdForm";
	}
	
	// 등록된 아이디로 인증코드 발송, 인증코드 입력폼 요청
	@PostMapping("/resetPwd")
	public String resetPwd2(String username, Model model) throws UnsupportedEncodingException, MessagingException {
		Optional<FundUser> FU = this.fundUserService.findByuserName(username);
		String code = emailService.sendEmail(FU.get().getEmail());
		model.addAttribute("code",code);
		
		return "/user/resetCodeForm";
	}

	
	
	
	
		
}
