package com.funding.fundArtistList;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundArtistListService {

	private final FundArtistListRepository fundArtistListRepository;
	
	// 펀드 아티스트 리스트
	public List<FundArtistList> getFundArtistList(){
		return this.fundArtistListRepository.findAll();
	}
}
