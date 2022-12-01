package com.funding.fundUser;

import java.sql.Date;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.funding.user.RegisterValidation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundUserService {
	
	private final FundUserRepository fundUserRepository;
	private final PasswordEncoder passwordEncoder;
	// 회원가입 , 유저 생성
	public void register(RegisterValidation vo) {
		
		FundUser fundUser = new FundUser();
		
		fundUser.setUsername(vo.getUsername());
		fundUser.setPassword(passwordEncoder.encode(vo.getPassword1()));
		fundUser.setNickname(vo.getNickname());
		fundUser.setEmail(vo.getEmail()+"@"+vo.getDomain());
		fundUser.setMobile("010"+vo.getMobile());
		fundUser.setAddress(vo.getAddr1()+vo.getAddr2()+vo.getAddr3()+vo.getAddr4());
		fundUser.setGender(vo.getGender());
		fundUser.setBirth(Date.valueOf(vo.getYear()+"-"+vo.getMonth()+"-"+vo.getDay()));	
		fundUser.setRole("user");
		
		this.fundUserRepository.save(fundUser);
		System.out.println("@@@@@@@@@펀드유저 회원가입 성공");
	}
	
	// userName 으로 계정정보 찾기
	public Optional<FundUser> findByuserName(String username) {
		Optional<FundUser> fundUser = fundUserRepository.findByusername(username);
		return fundUser;
	}
	
	// 비밀번호 재설정
	public void resetPwd(String username, String pwd) {
		
		Optional<FundUser> FU = this.fundUserRepository.findByusername(username);
		FU.get().setPassword(passwordEncoder.encode(pwd));
		
		this.fundUserRepository.save(FU.get());
		
	}
	
	
	//nickname 으로 정보 찾기
	public FundUser findBynickname(String nickname){
		Optional<FundUser> fundUser = fundUserRepository.findBynickname(nickname);
		return fundUser.get();
	}

	// 전화번호 수정
	public void resetMobile(FundUser fundUser, String mobile) {
		fundUser.setMobile(mobile);
		this.fundUserRepository.save(fundUser);
	}
}
