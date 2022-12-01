package com.funding.fundArtistList;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.funding.fundArtist.FundArtist;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundArtistListService {

	private final FundArtistListRepository fundArtistListRepository;
	
	// 펀드 아티스트 리스트
	public List<FundArtistList> findAll(){
		return this.fundArtistListRepository.findAll();
	}
	
	// 펀드 아티스트 참여
	public void join(Set<FundArtist> fundArtist) {
		FundArtistList fundArtistList = new FundArtistList();
		
		fundArtistList.setFundArtist(fundArtist);
		
		this.fundArtistListRepository.save(fundArtistList);
	}

	
}
