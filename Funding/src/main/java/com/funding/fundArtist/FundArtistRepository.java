package com.funding.fundArtist;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundArtistList.FundArtistList;

public interface FundArtistRepository extends JpaRepository<FundArtist, Integer>{
	Optional<FundArtist> findByusername(String username);
	Optional<FundArtist> findByNickname(String nickname);
	
	void save(FundArtistList fundArtistList);
}
