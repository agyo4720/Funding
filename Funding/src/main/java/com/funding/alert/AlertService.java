package com.funding.alert;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.funding.cancels.CancelsController;
import com.funding.cancels.CancelsService;
import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundArtistList.FundArtistList;
import com.funding.fundArtistList.FundArtistListService;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoard.FundBoardService;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundList.FundList;
import com.funding.fundTargetList.FundTargetList;
import com.funding.fundTargetList.FundTargetListService;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;
import com.funding.sale.Sale;
import com.funding.sale.SaleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AlertService {
	
	private final AlertRepository alertRepository;
	private final FundArtistService fundArtistService;
	private final FundUserService fundUserService;
	private final FundBoardService fundBoardService;
	private final FundTargetListService fundTargetListService;
	private final FundArtistListService fundArtistListService; 
	private final CancelsController cancelsController;
	private final CancelsService cancelsService;
	private final SaleService saleService;
	
	//지정펀딩에 댓글 생성시 알림 등록 (글 작성자, 댓글 쓴 사람, 내용)
	public void answerAlertTarget(FundBoardTarget fundBoardTarget, String principal, String content) {
		Optional<FundUser> user = fundUserService.findByuserName(principal);
		String url = "/fundTarget/detail/" + fundBoardTarget.getId();
		
		if(user.isEmpty()) {
			Optional<FundArtist> artist = fundArtistService.findByuserName(principal);
			
			
			Alert alert = new Alert();
			alert.setContent(content);
			alert.setUrl(url);
			alert.setWitchAlert("댓글");
			alert.setHostUser(fundBoardTarget.getFundUser());
			alert.setGuestArtist(artist.get());
			alert.setFundBoardTarget(fundBoardTarget);
			
			alertRepository.save(alert);
			return;
		}
		
		
		Alert alert = new Alert();
		alert.setContent(content);
		alert.setUrl(url);
		alert.setWitchAlert("댓글");
		alert.setHostUser(fundBoardTarget.getFundUser());
		alert.setGuestUser(user.get());
		alert.setFundBoardTarget(fundBoardTarget);
		
		alertRepository.save(alert);
	}
	
	
	//지정펀드 시간 마감시 알림 (해당펀딩, username)
	public void fundEndAlert(FundBoardTarget fundBoardTarget, String principal) {
		Optional<FundUser> user = fundUserService.findByuserName(principal);
		String url = "/fundTarget/detail/" + fundBoardTarget.getId();
		
		if(user.isEmpty()) {
			Optional<FundArtist> artist = fundArtistService.findByuserName(principal);
			
			
			Alert alert = new Alert();
			alert.setContent(fundBoardTarget.getSubject() + "<br/>펀딩기간 만료되었습니다.");
			alert.setUrl(url);
			alert.setWitchAlert("마감");
			alert.setHostArtist(artist.get());
			alert.setFundBoardTarget(fundBoardTarget);
			
			alertRepository.save(alert);
			return;
		}
		
		Alert alert = new Alert();
		alert.setContent(fundBoardTarget.getSubject() + "<br/>펀딩기간이 만료되었습니다.");
		alert.setUrl(url);
		alert.setWitchAlert("마감");
		alert.setHostUser(user.get());
		alert.setFundBoardTarget(fundBoardTarget);
		
		alertRepository.save(alert);
	}
	
	
	//지정펀딩 금액 달성시 알림 (해당펀딩, 해당 유저)
	public void fundEndAmount(FundBoardTarget fundBoardTarget, String principal) {
		Optional<FundUser> user = fundUserService.findByuserName(principal);
		String url = "/fundTarget/detail/" + fundBoardTarget.getId();
		
		Alert alert = new Alert();
		alert.setContent(fundBoardTarget.getSubject() + "<br/>펀딩 100% 달성");
		alert.setUrl(url);
		alert.setWitchAlert("펀딩");
		alert.setHostUser(user.get());
		alert.setFundBoardTarget(fundBoardTarget);
		
		alertRepository.save(alert);
	}
	
	
	
	
	
	
	
	
	
	
	//미지정펀딩에 댓글 생성시 알림 등록
	public void answerAlertBoard(FundBoard fundBoard, String principal, String content) {
		Optional<FundUser> user = fundUserService.findByuserName(principal);
		String url = "/fundBoard/detail/" + fundBoard.getId();
		
		
		if(user.isEmpty()) {
			Optional<FundArtist> artist = fundArtistService.findByuserName(principal);
			
			Alert alert = new Alert();
			alert.setContent(content);
			alert.setUrl(url);
			alert.setWitchAlert("댓글");
			alert.setHostUser(fundBoard.getFundUser());
			alert.setGuestArtist(artist.get());
			alert.setFundBoard(fundBoard);
			
			alertRepository.save(alert);
			
			return;
		}
		
		
		Alert alert = new Alert();
		alert.setContent(content);
		alert.setUrl(url);
		alert.setWitchAlert("댓글");
		alert.setHostUser(fundBoard.getFundUser());
		alert.setGuestUser(user.get());
		alert.setFundBoard(fundBoard);
		
		alertRepository.save(alert);
	}

	
	//미지정펀드 시간 마감시 알림 (해당펀딩, username)
	public void fundBoardEndAlert(FundBoard fundBoard, String principal) {
		Optional<FundUser> user = fundUserService.findByuserName(principal);
		String url = "/fundBoard/detail/" + fundBoard.getId();
		
		Alert alert = new Alert();
		alert.setContent(fundBoard.getSubject() + "<br/>펀딩기간이 만료되었습니다");
		alert.setUrl(url);
		alert.setWitchAlert("마감");
		alert.setHostUser(user.get());
		alert.setFundBoard(fundBoard);
		
		alertRepository.save(alert);
	}
	
	//미지정펀딩 금액 달성시 알림 (해당펀딩, 해당 유저)
	public void fundBoardEndAmount(FundBoard fundBoard, String principal) {
		Optional<FundUser> user = fundUserService.findByuserName(principal);
		String url = "/fundBoard/detail/" + fundBoard.getId();
		
		Alert alert = new Alert();
		alert.setContent(fundBoard.getSubject() + "<br/>펀딩 100% 달성");
		alert.setUrl(url);
		alert.setWitchAlert("펀딩");
		alert.setHostUser(user.get());
		alert.setFundBoard(fundBoard);
		
		alertRepository.save(alert);
	}
	
	
	//미지정 펀딩기간 마감 + 100%시 공연자 당선
	public void fundBoardSuccess(FundBoard fundBoard) throws Exception {
		LocalDate d1 = LocalDate.parse("2022-12-25",DateTimeFormatter.ISO_DATE);
		
		//펀딩마감 되면 실행
		if(fundBoard.getFundDuration().isBefore(LocalDate.now())) {
		
			List<FundArtistList> faList = fundArtistListService.findByFundBoard(fundBoard);
			
			if(faList.size() > 0) {
				log.info("아티스트 있어서 추려냄");
				Set<FundUser> sUser = new HashSet<>();
				int index = 0;
				FundArtistList fundArtistList = faList.get(0); 
				//투표 수 가장 많은 사람 찾아내기
				for(int i=0; i<faList.size(); i++) {
					if(faList.get(i).getFundUserList().size() > sUser.size()) {
						sUser = faList.get(i).getFundUserList();
						fundArtistList = faList.get(i);
						index = i;
					}
				}
				//공연 상태 변경
				FundBoard fundBaord = faList.get(index).getFundBoard();
				fundBoard.setState("공연");
				//나머지 아티스트 제거
				fundBoardService.addFundBoard(fundBaord);
				faList.remove(index);
				fundArtistListService.deleteList(faList);
				//알림추가
				modifyBoardAlert(fundArtistList);
				
			//아티스트가 아무도 없으면 환불
			}else {
				log.info("아티스트 없어서 환불");
				List<Sale> sList = saleService.findByFundBoard(fundBoard.getSubject());
				for(Sale s : sList) {
					Optional<FundUser> fUser = fundUserService.findByuserName(s.getUsername());
					
					cancelsController.totalCancel(s.getPayCode(), "<br/>아티스트 미참여로 인한 환불");
					cancelsService.cancelInfo(
							s.getOrederId()
							,s.getPayMoney()
							,s.getOrderName()
							,"아티스트 없어용"
							,fUser
							,s.getPayCode());
					fundBoardEndAlert(fundBoard, s.getUsername());
					
				}
				
			}
		}
		
	}
	
	
	
	
	
	public List<Alert> findByHostUser(FundUser user){
		List<Alert> aList = alertRepository.findByHostUser(user);
		return aList;
	}
	
	public List<Alert> findByHostArtist(FundArtist art){
		List<Alert> aList = alertRepository.findByHostArtist(art);
		return aList;
	}
	
	public void deleteAlert(Integer id) {
		Optional<Alert> alert = alertRepository.findById(id);
		alertRepository.delete(alert.get());
	}
	
	//지정 삭제시 삭제됬다는 알림
	public void deleteTargetThenAlert(List<FundTargetList> fundTargetList) {
		
		for(FundTargetList fl : fundTargetList) {
			Alert alert = new Alert();
			alert.setContent(fl.getFundBoardTarget().getSubject() + "<br/>게시글이 삭제되어 펀딩이 취소되었습니다.");
			alert.setHostUser(fl.getFundUser());
			alert.setWitchAlert("취소");
			
			alertRepository.save(alert);
		}
	}
	
	//미지정 삭제시 삭제 됬다는 알림
	public void deleteBoardThenAlert(List<FundList> fundList) {
		for(FundList fl : fundList) {
			Alert alert = new Alert();
			alert.setContent(fl.getFundBoard().getSubject() + "<br/>게시글이 삭제되어 펀딩이 취소되었습니다.");
			alert.setHostUser(fl.getFundUser());
			alert.setWitchAlert("취소");
			
			alertRepository.save(alert);
		}
	}
	
	//아티스트 선정 후 수정 url 주기
	public void modifyBoardAlert(FundArtistList fundArtistList) {
		Alert alert = new Alert();
		alert.setContent(fundArtistList.getFundBoard().getSubject() + "<br/>펀딩에 선정 되었습니다.<br/>공연세부정보를 작성해 주세요!");
		alert.setHostArtist(fundArtistList.getFundArtist());
		alert.setWitchAlert("수정");
		alert.setUrl("/fundBoard/modify/" + fundArtistList.getFundBoard().getId());
		
		alertRepository.save(alert);
	}
	
	
		
}