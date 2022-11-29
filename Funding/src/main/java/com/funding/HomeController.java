package com.funding;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;

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
import com.funding.user.mailValidation.EmailService;

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
	
	
	
	// 이메일 인증 테스트
	private final EmailService emailService;
	
	@RequestMapping("/emailAuth")
	@ResponseBody
	public String emailTest(String email) throws UnsupportedEncodingException, MessagingException {
		String emailAuthCode = emailService.sendEmail(email);
		
		return emailAuthCode;
	}
	
	// 세션 테시트
//	@RequestMapping("/test")
//	public String sessionTest(HttpSession session, Principal principal) {
//		
//		Optional<FundUser> FU = fundUserService.findByuserName(principal.getName());
//		
//		session.setAttribute("user", FU.get());
//		Object gg = session.getAttribute("user");
//		System.out.println(gg);
//		return "redirect:/";
//	}
}
