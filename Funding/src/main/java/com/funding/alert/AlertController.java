package com.funding.alert;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.funding.cancels.CancelsController;
import com.funding.cancels.CancelsService;
import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoard.FundBoardService;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundBoardTarget.FundTargetService;
import com.funding.fundList.FundList;
import com.funding.fundList.FundListService;
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
	private final FundBoardService fundBoardService;
	private final SaleRepository saleRepository;
	private final CancelsController cancelsController;
	private final CancelsService cancelsService;
	private final FundListService fundListService;
	
	
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
				map.put("content", artList.get(i).getContent());
				map.put("url", artList.get(i).getUrl());
				map.put("type", artList.get(i).getWitchAlert());
				alretList.add(map);
			}
			
			return alretList;
		}
		

		
		//유저일 때, 댓글 알림추가
		List<Alert> aList = alertService.findByHostUser(user.get());
		List<HashMap<String, String>> alretList = new ArrayList<>();
		for(int i=0; i<aList.size(); i++) {
			HashMap<String, String> map = new HashMap<>();
			map.put("alertId", aList.get(i).getId().toString());
			
			boolean guestcheck = true;
			if(aList.get(i).getGuestUser() != null) {
				map.put("Guestname", aList.get(i).getGuestUser().getUsername());
			}else if(aList.get(i).getGuestArtist() != null) {
				map.put("Guestname", aList.get(i).getGuestArtist().getUsername());
			}else {
				guestcheck = false;
			}
			
			
			//접속자와 댓글 생성자 같으면 출력 안함
			if(guestcheck) {
				if(aList.get(i).getGuestUser() != null) {
					String userequals = aList.get(i).getGuestUser().getUsername();
					if(user.get().getUsername().equals(userequals)) {
						continue;
					}
				}else if(aList.get(i).getGuestArtist() != null) {
					String userequals = aList.get(i).getGuestArtist().getUsername();
					if(user.get().getUsername().equals(userequals)) {
						continue;
					}
				}
			}
			map.put("content", aList.get(i).getContent());
			map.put("url", aList.get(i).getUrl());
			map.put("type", aList.get(i).getWitchAlert());
			alretList.add(map);
		}
		
		return alretList;
	}
	
	
	//지정펀딩 기간 마감시 업데이트
	@RequestMapping("/update")
	@ResponseBody
	public String fundEndDate(@RequestParam("user")String username) throws Exception{
		List<FundBoardTarget> targetList = fundTargetService.findAllList();
		
		for(int i=0; i<targetList.size(); i++) {
			
			//펀딩기간 만료시 알림
			LocalDate d1 = LocalDate.parse("2022-12-25",DateTimeFormatter.ISO_DATE);
			if(targetList.get(i).getFundDurationE().isBefore(LocalDate.now()) &&
					targetList.get(i).getStatus().equals("진행중")) {
				
				log.info("날짜 지났어용    : " + targetList.get(i).getSubject());
				log.info("게시글 만료 날짜 : "+ targetList.get(i).getFundDurationE());
				log.info("비교하는 날짜    : " + LocalDate.now());
				targetList.get(i).setStatus("만료됨");
				fundTargetService.addTargetFund(targetList.get(i));
				
				//기간 지나고 100% 미달성시 환불 시킴
				if(targetList.get(i).getFundCurrent() < targetList.get(i).getFundAmount()) {
					List<Sale> sale = saleRepository.findByFundBoardTarget(targetList.get(i).getSubject());
					for(int j=0; j<sale.size(); j++){
						if(sale.get(j).getCheckin().equals("결제완료")) {
							
							cancelsController.totalCancel(sale.get(j).getPayCode(),"기간만료 환불");
							cancelsService.totalCancelInfo(
									sale.get(j).getOrederId()
									, Integer.valueOf(sale.get(j).getPayMoney()).intValue()
									, sale.get(j).getOrderName()
									, "기간만료 환불"
									, sale.get(j).getFundUser()
									, sale.get(j).getUsername());
						}
					}
				}
				
				
				//환불 알림 등록, 리스트에서 삭제
				FundBoardTarget fundBoardTarget = targetList.get(i);
				List<FundTargetList> fListList = fundTargetListService.findByFundBoardTarget(fundBoardTarget);
				for(int j=0; j<fListList.size(); j++) {
					FundUser user = fListList.get(j).getFundUser();
					fundTargetListService.delete(user, fundBoardTarget);
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
			}else if(targetList.get(i).getFundCurrent() < targetList.get(i).getFundAmount() && 
					targetList.get(i).getStatus().equals("100%⇑⇑⇑")) {
				targetList.get(i).setStatus("진행중");
				fundTargetService.addTargetFund(targetList.get(i));
				
			}
		}
		
		//지정펀딩 공연 날짜 7일 지나면 리스트에서 삭제
		FundUser user1 = fundUserService.findByuserName(username).get();
		List<FundTargetList> tList = fundTargetListService.findByFundUser(user1);
		
		//DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		//LocalDateTime testDate = LocalDateTime.parse("2023-12-25 12:00", format1);
		//LocalDateTime.now().plusDays(7);
		for(FundTargetList i : tList) {
			if(i.getFundBoardTarget().getStartDate().plusDays(7).isBefore(LocalDateTime.now())) {
				fundTargetListService.delete(user1, i.getFundBoardTarget());
			}
		}
		
		
		
		
		
		
		
		//미지정 알림업데이트
		List<FundBoard> bList = fundBoardService.findAllList();
			
		for(int i=0; i<bList.size(); i++) {
				
			//펀딩기간 만료시 알림
			LocalDate d12 = LocalDate.parse("2023-12-05",DateTimeFormatter.ISO_DATE);
			if(bList.get(i).getFundDuration().isBefore(LocalDate.now()) &&
					bList.get(i).getState().equals("진행중")) {
				
				log.info("날짜 지났어용    : " + bList.get(i).getSubject());
				log.info("게시글 만료 날짜 : "+ bList.get(i).getFundDuration());
				log.info("비교하는 날짜    : " + LocalDate.now());
				bList.get(i).setState("만료됨");
				fundBoardService.addFundBoard(bList.get(i));
				
				//기간 지나고 100% 미달성시 환불 시킴
				if(bList.get(i).getFundCurrent() < bList.get(i).getFundAmount()) {
					List<Sale> sale = saleRepository.findByFundBoard(bList.get(i).getSubject());
					for(int j=0; j<sale.size(); j++){
						if(sale.get(j).getCheckin().equals("결제완료")) {
							
							cancelsController.totalCancel(sale.get(j).getPayCode(),"기간만료 환불");
							cancelsService.totalCancelInfo(
									sale.get(j).getOrederId()
									, Integer.valueOf(sale.get(i).getPayMoney()).intValue()
									, sale.get(i).getOrderName()
									, "기간만료 환불"
									, sale.get(i).getFundUser()
									, sale.get(i).getUsername());
						}
					}
				}
				
				//환불 알림 등록, 리스트에서 삭제
				FundBoard fundBoard = bList.get(i);
				List<FundList> fList = fundListService.findByFundBoard(fundBoard);
				for(int j=0; j<fList.size(); j++) {
					FundUser user = fList.get(j).getFundUser();
					fundListService.deleteFund(user, fundBoard);
					alertService.fundBoardEndAlert(fundBoard, user.getUsername());
				}
			}
			
			
			//펀딩 금액 달성시
			if(bList.get(i).getFundCurrent() >= bList.get(i).getFundAmount() && 
					bList.get(i).getState().equals("진행중")) {
				
			
				log.info("해당펀딩이름 : " + bList.get(i).getSubject() );
				log.info("펀딩금액    : " + bList.get(i).getFundCurrent() );
				log.info("달성금액    : " + bList.get(i).getFundAmount() );
				
				bList.get(i).setState("100%⇑⇑⇑");
				fundBoardService.addFundBoard(bList.get(i));
				
				//알림 등록
				FundBoard fundBoard = bList.get(i);
				List<FundList> fList = fundListService.findByFundBoard(fundBoard);
				for(int j=0; j<fList.size(); j++) {
					FundUser user = fList.get(j).getFundUser();
					alertService.fundBoardEndAmount(fundBoard, user.getUsername());
				}
			}else if(bList.get(i).getFundCurrent() < bList.get(i).getFundAmount() && 
					bList.get(i).getState().equals("100%⇑⇑⇑")) {
				bList.get(i).setState("진행중");
				fundBoardService.addFundBoard(bList.get(i));
			}
		}
		
		
		
		
		//미지정펀딩 공연 날짜 7일 지나면 리스트에서 삭제
		List<FundList> fbList = fundListService.findByFundUser(user1);
		
		//DateTimeFormatter format1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
		//LocalDateTime testDate = LocalDateTime.parse("2023-12-25 12:00", format1);
		//LocalDateTime.now().plusDays(7);
		for(FundList i : fbList) {
			if(i.getFundBoard().getStartDateTime().plusDays(7).isBefore(LocalDateTime.now())) {
				fundListService.deleteFund(user1, i.getFundBoard());
			}
		}
		
			
		return "알림 정리 했어요";
	}
	
	
	
	
	

}