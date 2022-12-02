package com.funding.alert;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.cancels.CancelsController;
import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundTargetList.FundTargetList;
import com.funding.fundTargetList.FundTargetListService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;
import com.funding.sale.Sale;
import com.funding.sale.SaleRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/alert")
@RequiredArgsConstructor
@Controller
public class AlertController {

	private final AlertService alertService;
	private final FundUserService fundUserService;
	private final FundArtistService fundArtistService;
	private final FundTargetListService fundTargetListService;
	private final FundTargetService fundTargetService;
	private final SaleRepository saleRepository;
	private final CancelsController cancelsController;
	
	
	//댓글 알림 불러오기 (ajax)
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
				
				if(artList.get(i).getGuestUser() != null) {
					map.put("Guestname", artList.get(i).getGuestUser().getUsername());
				}else if(artList.get(i).getGuestArtist() != null) {
					map.put("Guestname", artList.get(i).getGuestArtist().getUsername());
				}
				
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
			
			if(aList.get(i).getGuestUser() != null) {
				map.put("Guestname", aList.get(i).getGuestUser().getUsername());
			}else if(aList.get(i).getGuestArtist() != null) {
				map.put("Guestname", aList.get(i).getGuestArtist().getUsername());
			}
					
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
	
	
	//펀딩 기간 마감시 업데이트
	@RequestMapping("/update")
	@ResponseBody
	public String fundEndDate() throws Exception{
		List<FundBoardTarget> targetList = fundTargetService.findAllList();
		
		for(int i=0; i<targetList.size(); i++) {
			
			//펀딩기간 만료시 알림
			//LocalDate d1 = LocalDate.parse("2022-12-05",DateTimeFormatter.ISO_DATE);
			if(targetList.get(i).getFundDurationE().isBefore(LocalDate.now()) &&
					targetList.get(i).getStatus().equals("진행중")) {
				
				log.info("날짜 지났어용    : " + targetList.get(i).getSubject());
				log.info("게시글 만료 날짜 : "+ targetList.get(i).getFundDurationE());
				log.info("비교하는 날짜    : " + LocalDate.now());
				targetList.get(i).setStatus("만료됨");
				fundTargetService.addTargetFund(targetList.get(i));
				
				//환불 시킴
				List<Sale> sale = saleRepository.findByFundBoardTarget(targetList.get(i).getSubject());
				for(int j=0; i<sale.size(); j++){
					sale.get(j).getPayCode();
					sale.get(j).setCheckin("게시글 삭제");
					
					cancelsController.totalCancel(sale.get(j).getPayCode(),"게시글 삭제");
				}
				
				//알림 등록
				FundBoardTarget fundBoardTarget = targetList.get(i);
				List<FundTargetList> fListList = fundTargetListService.findByFundBoardTarget(fundBoardTarget);
				for(int j=0; j<fListList.size(); j++) {
					FundUser user = fListList.get(j).getFundUser();
					alertService.fundEndAlert(fundBoardTarget, user.getUsername());
				}
			}
			
			//펀딩 금액 달성시
			if(targetList.get(i).getFundCurrent() >= targetList.get(i).getFundAmount() && 
					targetList.get(i).getStatus().equals("진행중")) {
				
			
				log.info("해당펀딩이름 : " + targetList.get(i).getSubject() );
				log.info("펀딩금액    : " + targetList.get(i).getFundCurrent() );
				log.info("달성금액    : " + targetList.get(i).getFundAmount() );
				
				targetList.get(i).setStatus("100%⇑⇑⇑");
				fundTargetService.addTargetFund(targetList.get(i));
				
				//알림 등록
				FundBoardTarget fundBoardTarget = targetList.get(i);
				List<FundTargetList> fListList = fundTargetListService.findByFundBoardTarget(fundBoardTarget);
				for(int j=0; j<fListList.size(); j++) {
					FundUser user = fListList.get(j).getFundUser();
					alertService.fundEndAmount(fundBoardTarget, user.getUsername());
				}
			}
		}
		return "알림 정리 했어요";
	}
	
	
	
	
	

}
