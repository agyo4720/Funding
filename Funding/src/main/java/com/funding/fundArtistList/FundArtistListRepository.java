package com.funding.fundArtistList;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundArtist.FundArtist;
import com.funding.fundBoard.FundBoard;

public interface FundArtistListRepository extends JpaRepository<FundArtistList, Integer>{

	public List<FundArtistList> findByFundArtist(FundArtist fundArtist);
	
	public List<FundArtistList> findByFundBoard(FundBoard fundBoard);
}
