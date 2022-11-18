package com.funding.user;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.fundArtist.FundArtistService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

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
		
//		try {
//			this.fundUserService.register(vo);
//			return "main/home";
//		}catch(Exception err) {
//			this.fundArtistService.register(vo);
//			return "main/home";
//		}
        
        
		if(vo.getRole().equals("user")) {
			this.fundUserService.register(vo);
		}else if(vo.getRole().equals("artist")){
			this.fundArtistService.register(vo);
		}
		
		return "main/home";
		
	}
	
	// id 중복검사
	@PostMapping("/idCheck")
	@ResponseBody
	public Map<String, Object> idCheck(FundUser vo) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		
        // 010-0000-0000
        System.out.println(vo.getUsername());
        
        String uid = vo.getUsername();
        
        // 응답 데이터 셋팅
        result.put("code", vo.getUsername());
        
//        this.fundUserService.findByuserName(uid);
//        this.fundArtistService.findByuserName(uid);
        
        return result;
	}
	
}
