package com.funding.user;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;
import com.funding.user.mailValidation.EmailService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/user")
@Controller
@RequiredArgsConstructor
public class RegisterController {

	private final FundUserService fundUserService;
	private final FundArtistService fundArtistService;
	
	// 회원가입 폼 요청
	@GetMapping("/register")
	public String registerForm(RegisterValidation registerValidation) {
		return "user/userCreateForm";
	}
	
	// 회원가입 , 유저정보 저장
	@PostMapping("/register")
	public String register(@Valid RegisterValidation vo, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "user/userCreateForm";
		}
		
        if (!vo.getPassword1().equals(vo.getPassword2())) {
            bindingResult.reject("passwordInCorrect", 
                    "2개의 패스워드가 일치하지 않습니다.");
            return "user/userCreateForm";
        }        
        
		if(vo.getRole().equals("user")) {
			this.fundUserService.register(vo);
		}else if(vo.getRole().equals("artist")){
			this.fundArtistService.register(vo);
		}
		
		return "redirect:/user/login";
		
	}
	
	// id 중복검사
	@PostMapping("/usernameCheck")
	@ResponseBody
	public Map<String, Object> idCheck(FundUser vo) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
        System.out.println(vo.getUsername());
        
        String uid = vo.getUsername();
        

        Optional<FundUser> FU = this.fundUserService.findByuserName(uid);
        Optional<FundArtist> FA = this.fundArtistService.findByuserName(uid);

        
        if(FU.isPresent()) {
        	result.put("code", "사용중인 아이디입니다");
        	return result;
        }
        
        if(FA.isPresent()) {
        	result.put("code", "사용중인 아이디입니다");
        	return result;
        }
        
        
        // 응답 데이터 셋팅
        result.put("code", "사용 가능한 ID 입니다");
        return result;
	}
	
	// 이메일 인증
	private final EmailService emailService;
		
		@RequestMapping("/emailAuth")
		@ResponseBody
		public String emailTest(String email) throws UnsupportedEncodingException, MessagingException {
			String emailAuthCode = emailService.sendEmail(email);
			
			return emailAuthCode;
	}
	
	
}
