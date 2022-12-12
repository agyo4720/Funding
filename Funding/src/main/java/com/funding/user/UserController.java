package com.funding.user;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;
import com.funding.selfBoard.SelfBoard;
import com.funding.selfBoard.SelfBoardService;
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
	private final PasswordEncoder passwordEncoder;
	private final SelfBoardService selfBoardService;
	
	String username = null;
	String userRole = null;
	
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
	
	//계좌관리에 사이드바 사용
	@RequestMapping("/myInfo/ajax")
	@ResponseBody
	public String myInfoAjax(@RequestParam("artist") String artist) {
		Optional<FundArtist> FA = this.fundArtistService.findByuserName(artist);
		return FA.get().getUsername();
	}
	
	//프로필 작성 유무에 따른 링크 변경
	@RequestMapping("/myInfo/profile")
	@ResponseBody
	public String profileAjax(@RequestParam("artist") String artist) {
		Optional<SelfBoard> SB = this.selfBoardService.findByUsername(artist);
		
		if(SB.isPresent()) {
			return "1";
		}
		if(SB.isEmpty()) {
			return "0";
		}
		
		return "";
		
	}
	
	// 비밀번호 초기화 하기위한 id 입력 폼 요청
	@GetMapping("/resetPwd")
	public String resetPwd() {
		
		return "/user/resetPwdForm";
	}
	
	// 등록된 아이디로 인증코드 발송, 인증코드 입력폼 요청
	@PostMapping("/resetPwd")
	@ResponseBody
	public String resetPwd2(String username, Model model) throws UnsupportedEncodingException, MessagingException {
		Optional<FundUser> FU = this.fundUserService.findByuserName(username);
		Optional<FundArtist> FA = this.fundArtistService.findByuserName(username);
		String code = null;
		
		if(FU.isPresent()) {
			code = emailService.sendEmail(FU.get().getEmail());
			this.userRole = "user";
		} else if(FA.isPresent()) {
			code = emailService.sendEmail(FA.get().getEmail());
			this.userRole = "artist";
		}
		this.username = username;
		
		
		return code;		
	}
	
	
	// 비밀번호 수정
	@PostMapping("/resetPwdConfirm")
	public String resetPwdConfirm2(String pwd){

		if(userRole.equals("user")) {
			this.fundUserService.resetPwd(this.username, pwd);
		}
		
		if(userRole.equals("artist")) {
			this.fundArtistService.resetPwd(this.username, pwd);
		}
		
		return "redirect:/user/login";
	}
	
	// 비밀번호 수정2
	@PostMapping("/resetPwdConfirm2")
	public String resetPwdConfirm3(String pwd, Principal principal){

		Optional<FundUser> FU = this.fundUserService.findByuserName(principal.getName());
		Optional<FundArtist> FA = this.fundArtistService.findByuserName(principal.getName());
		
		if(FU.isPresent()) {
			this.fundUserService.resetPwd(FU.get().getUsername(), pwd);
		}
		
		if(FA.isPresent()) {
			this.fundArtistService.resetPwd(FA.get().getUsername(), pwd);
		}
		
		
		return "redirect:/user/myInfo";
	}
	
	// 전화번호 수정
	@PostMapping("/resetMobile")
	public String resetMobile(Principal principal, String mobile) {
		Optional<FundUser> FU = this.fundUserService.findByuserName(principal.getName());
		Optional<FundArtist> FA = this.fundArtistService.findByuserName(principal.getName());
		if(FU.isPresent()) {
			this.fundUserService.resetMobile(FU.get(),mobile);
		}
		if(FA.isPresent()) {
			this.fundArtistService.resetMobile(FA.get(),mobile);
		}
		
		return "redirect:/user/myInfo";
	}

	
	
	
	
		
}
