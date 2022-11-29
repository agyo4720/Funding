package com.funding.fundTargetList;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.alert.AlertService;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class FundTargetListController {

	private final FundTargetListService fundTargetListService;
	private final FundUserService fundUserService;
	private final FundTargetService fundTargetService;
	private final AlertService alertService;
	
	
	//지정펀딩 목록 불러오기(ajax)
	@RequestMapping("/show/fundList")
	@ResponseBody
	public List<HashMap<String, String>> currentFundList(@RequestParam("user")String username) {
		
		Optional<FundUser> user = fundUserService.findByuserName(username);
		
		
		//현재 펀딩목록 추가
		List<FundTargetList> fList = fundTargetListService.findByFundUser(user.get());
		List<HashMap<String, String>> fundList = new ArrayList<>();
		for(int i=0; i<fList.size(); i++) {
			HashMap<String, String> fmap = new HashMap<String, String>();
			
			//펀딩기간 만료시 알림
			LocalDate d1 = LocalDate.parse("2022-12-05",DateTimeFormatter.ISO_DATE);
			if(fList.get(i).getFundBoardTarget().getFundDurationE().isBefore(LocalDate.now()) &&
					fList.get(i).getFundBoardTarget().getStatus().equals("진행중")) {
				log.info("날짜 지났어용 : " + fList.get(i).getFundBoardTarget().getSubject());
				log.info("게시글 만료 날짜 : "+ fList.get(i).getFundBoardTarget().getFundDurationE());
				log.info("비교하는 날짜    : " + LocalDate.now());
				fList.get(i).getFundBoardTarget().setStatus("만료됨");
				fundTargetService.addTargetFund(fList.get(i).getFundBoardTarget());
				
				//알림 등록
				alertService.fundEndAlert(fList.get(i).getFundBoardTarget(), username);
			}
			
			fmap.put("fundName", fList.get(i).getFundBoardTarget().getSubject());
			fmap.put("status", fList.get(i).getFundBoardTarget().getStatus());
			double now = fList.get(i).getFundBoardTarget().getFundCurrent();
			double max = fList.get(i).getFundBoardTarget().getFundAmount();
			double percent = now / max * 100.0;
			fmap.put("percent", String.format("%.2f",percent) + "%");
			fmap.put("url", "/fundTarget/detail/" + fList.get(i).getFundBoardTarget().getId());
			
			fundList.add(fmap);
		}
		
		return fundList;
	}
}
