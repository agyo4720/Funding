package com.funding.fundList;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundListService {

	private final FundListRepository fundListRepository;
	
	public List<FundList> getFundList(){
		return this.fundListRepository.findAll();
	}
	
	
}
