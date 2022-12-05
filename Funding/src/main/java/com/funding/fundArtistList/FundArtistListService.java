package com.funding.fundArtistList;

import java.security.Principal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistRepository;
import com.funding.fundBoard.FundBoard;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundArtistListService {

	private final FundArtistListRepository fundArtistListRepository;
	private final FundArtistRepository fundArtistRepository;
	private final FundUserRepository fundUserRepository;
	
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
		
		fundArtistLists.setLikeConut(0);
		fundArtistLists.setFundArtist(fundArtist);
		fundArtistLists.setFundBoard(fundBoard);
		
		this.fundArtistListRepository.save(fundArtistLists);
	}
	
	
	public List<FundArtistList> findByFundBoard(FundBoard fundBoard) {
		
		return this.fundArtistListRepository.findByFundBoard(fundBoard);
	}
	
	// 해당 id로 데이터 가져오기
	public FundArtistList findById(Integer id) {
		
		Optional<FundArtistList> fundArtistList = this.fundArtistListRepository.findById(id);
		
		return fundArtistList.get();
	}
	
//	// 미지정 펀드 아티스트 투표하기
//	public void score(FundArtistList fundArtistList, FundUser fundUser) {
//		
//		List<FundArtistList> fal = this.fundArtistListRepository.findByFundUser(fundUser);
//
//		Integer i = fundArtistList.getLikeConut();
//		i++;
//		
//		fundArtistList.setLikeConut(i);
//		
//		this.fundArtistRepository.save(fundArtistList);
//		
//	}
	
	// 미지정 펀드 아티스트 투표하기
	public void addvote(FundBoard fundBoard, FundArtist fundArtist, FundUser fundUser, Integer id) {
		
		//List<FundArtistList> fList = this.fundArtistListRepository.findByFundBoardAndFundArtist(fundBoard, fundArtist);
		Optional<FundArtistList> fundArtistList = fundArtistListRepository.findById(id);
		Set<FundUser> sUser = fundArtistList.get().getFundUserList();
		sUser.add(fundUser);
		fundArtistList.get().setFundUserList(sUser);
		
		this.fundArtistListRepository.save(fundArtistList.get());
				
	}
	
	public void findByFundBoardAndFundArtist() {
		
		
	}

}
