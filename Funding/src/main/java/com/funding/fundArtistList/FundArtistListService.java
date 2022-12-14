package com.funding.fundArtistList;

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
		
		FundArtistList fundArtistLists = new FundArtistList();
		
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
	
	// 미지정 펀드 아티스트 투표하기
	public void addvote(
			FundBoard fundBoard,
			FundArtist fundArtist,
			FundUser fundUser,
			Integer id) {
		
		Optional<FundArtistList> fundArtistList = fundArtistListRepository.findById(id);
		
		Set<FundUser> sUser = fundArtistList.get().getFundUserList();
	
		sUser.add(fundUser);
		
		fundArtistList.get().setFundUserList(sUser);
		
		this.fundArtistListRepository.save(fundArtistList.get());
				
	}
	
	// 미저정펀딩 참여 중복조회
	public Optional<FundArtistList> search(FundArtist fundArtist, FundBoard fundBoard) {
		
		Optional<FundArtistList> fal = this.fundArtistListRepository.findByFundBoardAndFundArtist(fundBoard, fundArtist);
		
		return fal;
	
	}

	// 아티스트로 찾기
	public List<FundArtistList> findByFundArtist(FundArtist artist) {
		
		List<FundArtistList> faList = fundArtistListRepository.findByFundArtist(artist);
		
		return faList;
	}
	
	// 해당 리스트 지우기
	public void deleteList(List<FundArtistList> faList) {
		
		fundArtistListRepository.deleteAll(faList);
		
	}
	
	public void delete(FundArtistList fundArtistList) {
		fundArtistListRepository.delete(fundArtistList);
	}


}
