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

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundTargetList.FundTargetList;
import com.funding.fundTargetList.FundTargetListService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequestMapping("/alert")
@RequiredArgsConstructor
@Controller
public class AlertController {

	private final AlertService alertService;
	private final FundUserService fundUserService;
	private final FundArtistService fundArtistService;
	private final FundTargetListService fundTargetListService;
	
	//댓글 알림 불러오기
	@RequestMapping("/show")
	@ResponseBody
	public List<HashMap<String, String>> showAlert(@RequestParam("user")String username) {
		Optional<FundUser> user = fundUserService.findByuserName(username);
		
		//아티스트 일 때
		if(user.isEmpty()) {
			Optional<FundArtist> art = fundArtistService.findByuserName(username);
			List<Alert> artList = alertService.findByHostArtist(art.get());
			List<HashMap<String, String>> alretList = new ArrayList<>();
			for(int i=0; i<artList.size(); i++) {
				HashMap<String, String> map = new HashMap<>();
				map.put("alertId", artList.get(i).getId().toString());
				map.put("username", artList.get(i).getGuestUser() == null ? 
						artList.get(i).getGuestArtist().getUsername() : artList.get(i).getGuestUser().getUsername());
				//접속자와 댓글 생성자 같으면 출력 안함
				if(art.get().getUsername().equals(map.get("username"))) {
					continue;
				}
				map.put("content", artList.get(i).getContent());
				map.put("url", artList.get(i).getUrl());
				map.put("type", artList.get(i).getWitchAlert());
				alretList.add(map);
				
				return alretList;
			}
		}
		

		
		//유저일 때, 댓글 알림추가
		List<Alert> aList = alertService.findByHostUser(user.get());
		List<HashMap<String, String>> alretList = new ArrayList<>();
		for(int i=0; i<aList.size(); i++) {
			HashMap<String, String> map = new HashMap<>();
			map.put("alertId", aList.get(i).getId().toString());
			map.put("username", aList.get(i).getGuestUser() == null ? 
					aList.get(i).getGuestArtist().getUsername() : aList.get(i).getGuestUser().getUsername());
			//접속자와 댓글 생성자 같으면 출력 안함
			if(user.get().getUsername().equals(map.get("username"))) {
				continue;
			}
			map.put("content", aList.get(i).getContent());
			map.put("url", aList.get(i).getUrl());
			map.put("type", aList.get(i).getWitchAlert());
			alretList.add(map);
		}
		
		return alretList;
	}
}
