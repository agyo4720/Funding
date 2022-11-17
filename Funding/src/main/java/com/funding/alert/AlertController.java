package com.funding.alert;

import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class AlertController {

	private final AlertService alertService;
	
}
