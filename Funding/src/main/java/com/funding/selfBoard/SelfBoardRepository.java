package com.funding.selfBoard;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.funding.fundArtist.FundArtist;

public interface SelfBoardRepository extends JpaRepository<SelfBoard, Integer>{

<<<<<<< HEAD
<<<<<<< HEAD
	Optional<SelfBoard> findByFundArtist(FundArtist fundArtist);
=======
	Optional<SelfBoard> findByFundArtist(FundArtist username);
>>>>>>> 06d4aa52b968978e032064d67137e33a72aa01b8
=======
	Optional<SelfBoard> findByFundArtist(FundArtist fundArtist);
>>>>>>> origin

}
