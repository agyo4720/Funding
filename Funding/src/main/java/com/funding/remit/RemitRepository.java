package com.funding.remit;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RemitRepository extends JpaRepository<Remit,Integer> {
	List<Remit> findBysubMallId(String subMallId);
	public Page<Remit> findBysubMallId(String subMallId,Pageable pageable);//페이징
}
