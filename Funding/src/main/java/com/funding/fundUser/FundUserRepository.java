package com.funding.fundUser;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FundUserRepository extends JpaRepository<FundUser,Integer>{
	Optional<FundUser> findByusername(String username);
	Optional<FundUser> findBynickname(String nickname);
	
	
	
}
