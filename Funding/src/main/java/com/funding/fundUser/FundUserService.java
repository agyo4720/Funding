package com.funding.fundUser;

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
		fundUser.setEmail(vo.getEmail());
		fundUser.setMobile(vo.getMobile());
		fundUser.setAddress(vo.getAddress());
		fundUser.setGender(vo.getGender());
		fundUser.setBirth(vo.getBirth());		
		fundUser.setRole("user");
		
		this.fundUserRepository.save(fundUser);
	}
	
	// userName 으로 계정정보 찾기
	public FundUser findByuserName(String username) {
		Optional<FundUser> fundUser = fundUserRepository.findByusername(username);
		return fundUser.get();
	}
	
}
