package com.funding.index;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.funding.FundArtist.FundArtistService;
import com.funding.FundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RegisterController {

	private FundUserService fundUserService;
	private FundArtistService fundArtistService;
	
	// 회원가입 폼 요청
	@GetMapping("/register")
	public String registerForm(RegisterValidation vo) {
		
		return "user/userCreateForm";
	}
	
	// 회원가입 , 유저정보 저장
	@PostMapping("/register")
	public String register(@Valid RegisterValidation vo, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "userCreateForm";
		}
		
        if (!vo.getPassword1().equals(vo.getPassword2())) {
            bindingResult.reject("passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "signup_form";
        }
		
		
		if(vo.getRole().equals("user")) {
			this.fundUserService.register(vo);
		}else if(vo.getRole().equals("artist")){
			this.fundArtistService.register(vo);
		}
		
		
		return "home";
	}
}
