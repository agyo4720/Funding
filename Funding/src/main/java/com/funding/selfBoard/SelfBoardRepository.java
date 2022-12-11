package com.funding.selfBoard;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundArtist.FundArtist;

public interface SelfBoardRepository extends JpaRepository<SelfBoard, Integer>{

	Optional<SelfBoard> findByFundArtist(FundArtist fundArtist);

}