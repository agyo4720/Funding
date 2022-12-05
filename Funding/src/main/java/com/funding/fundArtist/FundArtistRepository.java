package com.funding.fundArtist;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundArtistList.FundArtistList;

public interface FundArtistRepository extends JpaRepository<FundArtist, Integer>{
	Optional<FundArtist> findByusername(String username);

	void save(FundArtistList fundArtistList);
}
