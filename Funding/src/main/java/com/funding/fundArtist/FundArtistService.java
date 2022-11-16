package com.funding.fundArtist;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.funding.user.RegisterValidation;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundArtistService {
	
	private final FundArtistRepository fundArtistRepository;
	private final PasswordEncoder passwordEncoder;
	// 회원가입 , 유저 생성
	public void register(RegisterValidation vo) {
		
		FundArtist fundArtist = new FundArtist();
		
		fundArtist.setUsername(vo.getUsername());
		fundArtist.setPassword(passwordEncoder.encode(vo.getPassword1()));
		fundArtist.setEmail(vo.getEmail());
		fundArtist.setMobile(vo.getMobile());
		fundArtist.setRole("artist");
		
		this.fundArtistRepository.save(fundArtist);
	}
}
