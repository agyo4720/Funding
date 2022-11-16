package com.funding.fundList;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FundListService {

	private final FundListRepository fundListRepository;
}
