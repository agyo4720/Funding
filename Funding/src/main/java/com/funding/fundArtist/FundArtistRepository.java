package com.funding.fundArtist;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundArtistList.FundArtistList;

public interface FundArtistRepository extends JpaRepository<FundArtist, Integer>{
	Optional<FundArtist> findByusername(String username);
	Optional<FundArtist> findByNickname(String nickname);
<<<<<<< HEAD
	
	
=======

>>>>>>> 06d4aa52b968978e032064d67137e33a72aa01b8
	void save(FundArtistList fundArtistList);
}
