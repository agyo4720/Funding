package com.funding.payment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface EnrollRepository extends JpaRepository<Enroll,Integer> {
	List<Enroll> findBysubMallId(String subMallId);
}
