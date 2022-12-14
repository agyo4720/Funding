package com.funding.selfBoard;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SelfBoardService {
	
	
	private final SelfBoardRepository selfBoardRepository;
	private final FundArtistRepository fundArtistRepository;
	
	//pr 수정하기
	public void modify(String subject, String content, String genre, String path, FundArtist artiest) {
		
		SelfBoard selfBoard = this.selfBoardRepository.findByFundArtist(artiest).get();
		selfBoard.setSubject(subject);
		selfBoard.setContent(content);
		selfBoard.setGenre(genre);
		selfBoard.setFilePath(path);
		selfBoard.setFundArtist(artiest);
		
		selfBoardRepository.save(selfBoard);
	}
	
	//pr 작성하기
	public void create(String subject, String content, String genre, String path, FundArtist artiest) {

		SelfBoard selfBoard = new SelfBoard();
		
		selfBoard.setSubject(subject);
		selfBoard.setContent(content);
		selfBoard.setGenre(genre);
		selfBoard.setFilePath(path);
		selfBoard.setFundArtist(artiest);
		
		selfBoardRepository.save(selfBoard);
	}
	
	//id로 찾기
	public SelfBoard findById(Integer id) {
		Optional<SelfBoard> selfBoard = selfBoardRepository.findById(id);
		return selfBoard.get();
	}
	

	// 유저네임으로 찾기 // 중복에러뜸
	public Optional<SelfBoard> findByUsername(String username) {
		Optional<FundArtist> fundArtist = fundArtistRepository.findByusername(username);
		Optional<SelfBoard> selfBoard = selfBoardRepository.findByFundArtist(fundArtist.get());
		return selfBoard;
	}
		

	public SelfBoard findByFundArtist(FundArtist fundArtist) {
			
		return this.selfBoardRepository.findByFundArtist(fundArtist).get();
			
	}
	
	public Optional<SelfBoard> findByFundArtist2(FundArtist fundArtist) {
		
		return this.selfBoardRepository.findByFundArtist(fundArtist);
			
	}

}