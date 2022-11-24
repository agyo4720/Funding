package com.funding.alert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/alert")
@RequiredArgsConstructor
@Controller
public class AlertController {

	private final AlertService alertService;
	private final FundUserService fundUserService;
	
	
	@RequestMapping("/show")
	@ResponseBody
	public List<HashMap<String, String>> showAlert(@RequestParam("user")String username) {
		Optional<FundUser> user = fundUserService.findByuserName(username);
		List<Alert> aList = alertService.findByHostUser(user.get());
		List<HashMap<String, String>> alretList = new ArrayList<>();
		for(int i=0; i<aList.size(); i++) {
			HashMap<String, String> map = new HashMap<>();
			map.put("content", aList.get(i).getContent());
			map.put("url", aList.get(i).getUrl());
			alretList.add(map);
		}
		
		return alretList;
	}
}
