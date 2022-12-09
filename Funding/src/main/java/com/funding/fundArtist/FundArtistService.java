package com.funding.fundArtist;

import java.sql.Date;
import java.util.Optional;

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
		fundArtist.setEmail(vo.getEmail()+"@"+vo.getDomain());
		fundArtist.setMobile("010"+vo.getMobile());
		fundArtist.setAddress(vo.getAddr1()+vo.getAddr2()+vo.getAddr3()+vo.getAddr4());
		fundArtist.setGender(vo.getGender());
		fundArtist.setBirth(Date.valueOf(vo.getYear()+"-"+vo.getMonth()+"-"+vo.getDay()));
		fundArtist.setRole("artist");
		fundArtist.setLikeCount(0);

		this.fundArtistRepository.save(fundArtist);
		System.out.println("@@@@@@@@@아티스트 회원가입 성공");
	}

	// userName 으로 계정정보 찾기
	public Optional<FundArtist> findByuserName(String username) {
		Optional<FundArtist> fundArtist = fundArtistRepository.findByusername(username);
		return fundArtist;
	}

	// 비밀번호 재설정
	public void resetPwd(String username, String pwd) {

		Optional<FundArtist> FA = this.fundArtistRepository.findByusername(username);
		FA.get().setPassword(passwordEncoder.encode(pwd));

		this.fundArtistRepository.save(FA.get());

	}

	// 전화번호 수정
	public void resetMobile(FundArtist fundArtist, String mobile) {
		fundArtist.setMobile(mobile);
		this.fundArtistRepository.save(fundArtist);
	}

	
	// 해당 id로 데이터 찾기(박남규)
	public FundArtist findById(Integer id) {
		
		Optional<FundArtist> fundArtist = this.fundArtistRepository.findById(id);
		
		return fundArtist.get();
	}
	
}
