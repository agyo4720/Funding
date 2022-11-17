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
		fundArtist.setNickname(vo.getNickname());
		fundArtist.setEmail(vo.getEmail());
		fundArtist.setMobile(vo.getMobile());
		fundArtist.setAddress(vo.getAddress());
		fundArtist.setGender(vo.getGender());
		fundArtist.setBirth(vo.getBirth());		
		fundArtist.setRole("artist");
		fundArtist.setLikeCount(0);
		
		this.fundArtistRepository.save(fundArtist);
	}
}
