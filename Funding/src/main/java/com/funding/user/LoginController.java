package com.funding.user;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class LoginController {

	private final FundUserService fundUserService;
	
	// 로그인 폼 요청 
	@GetMapping("/login")
	public String loginForm() {
		
		return "user/loginForm";
	}
	
	
	
}
