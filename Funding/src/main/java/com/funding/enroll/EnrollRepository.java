package com.funding.enroll;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollRepository extends JpaRepository<Enroll,Integer> {
	List<Enroll> findBysubMallId(String subMallId); //고유 아이디일치하는 정보들
}
