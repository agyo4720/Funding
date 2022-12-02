package com.funding.fundList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.fundTargetList.FundTargetList;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/fundList")
public class FundListController {
	
	private final FundListService fundListService;
	private final FundUserService fundUserService;
	
	
	// 펀드 리스트 목록
	@RequestMapping("/list")
	public String list(Model model) {
		
		List<FundList> fundListList = this.fundListService.getFundList();
		model.addAttribute("fundListList", fundListList);
		
		return "fundList_list";
	}
	
	
	//미지정 목록 불러오기(ajax)
	@GetMapping("/show")
	@ResponseBody
	public List<HashMap<String, String>> showBoard(@RequestParam("user")String username) {
		
		Optional<FundUser> user = fundUserService.findByuserName(username);
		
		//현재 펀딩목록 추가
				List<FundList> fList = fundListService.findByFundUser(user.get());
				List<HashMap<String, String>> fundList = new ArrayList<>();
				for(int i=0; i<fList.size(); i++) {
					HashMap<String, String> fmap = new HashMap<String, String>();
					
					
					fmap.put("fundName", fList.get(i).getFundBoard().getSubject());
					fmap.put("status", fList.get(i).getFundBoard().getState());
					double now = fList.get(i).getFundBoard().getFundCurrent();
					double max = fList.get(i).getFundBoard().getFundAmount();
					double percent = now / max * 100.0;
					fmap.put("percent", String.format("%.2f",percent) + "%");
					fmap.put("url", "/fundBoard/detail/" + fList.get(i).getFundBoard().getId());
					
					fundList.add(fmap);
				}
		
		
		return fundList;
	}

}
