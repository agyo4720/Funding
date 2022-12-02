package com.funding.fundArtistList;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.funding.fundArtist.FundArtist;
import com.funding.fundBoard.FundBoard;

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
	public void join(FundArtist fundArtist, FundBoard fundBoard) {
		
		List<FundArtistList> fundArtistList = this.fundArtistListRepository.findByFundArtist(fundArtist);
		
		// fundArtistList를 반복문을 사용해서 같은 유저일 경우 반복문에서 빠져나가는 로직
		for(FundArtistList fal : fundArtistList) {
			
			if(fal.getFundBoard().getId().equals(fundBoard.getId())) {
				return ;
			}
		}
	
		FundArtistList fundArtistLists = new FundArtistList();
		
		fundArtistLists.setFundArtist(fundArtist);
		fundArtistLists.setFundBoard(fundBoard);
		
		this.fundArtistListRepository.save(fundArtistLists);
	}
	
	public List<FundArtistList> findByFundBoard(FundBoard fundBoard) {
		
		return this.fundArtistListRepository.findByFundBoard(fundBoard);
	}

	
}
