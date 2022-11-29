package com.funding.alert;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistService;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AlertService {
	
	private final AlertRepository alertRepository;
	private final FundArtistService fundArtistService;
	private final FundUserService fundUserService;
	
	
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
	
	
	//펀드 시간 마감시 알림 (해당펀딩, 해당 유저)
	public void fundEndAlert(FundBoardTarget fundBoardTarget, String principal) {
		Optional<FundUser> user = fundUserService.findByuserName(principal);
		String url = "/fundTarget/detail/" + fundBoardTarget.getId();
		
		if(user.isEmpty()) {
			Optional<FundArtist> artist = fundArtistService.findByuserName(principal);
			
			
			Alert alert = new Alert();
			alert.setContent(fundBoardTarget.getSubject() + " 펀딩기간이 만료되었습니다");
			alert.setUrl(url);
			alert.setWitchAlert("마감");
			alert.setHostArtist(artist.get());
			alert.setFundBoardTarget(fundBoardTarget);
			
			alertRepository.save(alert);
			return;
		}
		
		Alert alert = new Alert();
		alert.setContent(fundBoardTarget.getSubject() + " 펀딩기간이 만료되었습니다");
		alert.setUrl(url);
		alert.setWitchAlert("마감");
		alert.setHostUser(user.get());
		alert.setFundBoardTarget(fundBoardTarget);
		
		alertRepository.save(alert);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	//미지정펀딩에 댓글 생성시 알림 등록
	public void answerAlertBoard(FundBoard fundBoard, String principal, String content) {
		Optional<FundUser> user = fundUserService.findByuserName(principal);
		if(user.isEmpty()) {
			Optional<FundArtist> artist = fundArtistService.findByuserName(principal);
			
			
			String url = "/fundBoard/detail/" + fundBoard.getId();
			
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

		
		String url = "/fundBoard/detail/" + fundBoard.getId();
		
		Alert alert = new Alert();
		alert.setContent(content);
		alert.setUrl(url);
		alert.setWitchAlert("댓글");
		alert.setHostUser(fundBoard.getFundUser());
		alert.setGuestUser(user.get());
		alert.setFundBoard(fundBoard);
		
		alertRepository.save(alert);
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

		
}
