package com.funding.selfBoard;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundArtist.FundArtist;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SelfBoardService {
	
	
	private final SelfBoardRepository selfBoardRepository;
	
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

}
