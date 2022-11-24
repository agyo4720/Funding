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
	
	
	//지정펀딩에 댓글 생성시 알림 등록
	public void answerAlertTarget(FundBoardTarget fundBoardTarget, Principal principal) {
		Optional<FundUser> user = fundUserService.findByuserName(principal.getName());
		if(user.isEmpty()) {
			Optional<FundArtist> artist = fundArtistService.findByuserName(principal.getName());
			
			String content = fundBoardTarget.getSubject() + "의 글에서 " + artist.get().getNickname() +
					"님이 댓글을 달았습니다.";
			
			String url = "/fundTarget/detail/" + fundBoardTarget.getId();
			
			Alert alert = new Alert();
			alert.setContent(content);
			alert.setUrl(url);
			alert.setHostUser(fundBoardTarget.getFundUser());
			alert.setGuestArtist(artist.get());
			alert.setFundBoardTarget(fundBoardTarget);
			
			alertRepository.save(alert);
			
			return;
		}
		String content = fundBoardTarget.getSubject() + "의 글에서 " + user.get().getNickname() +
				"님이 댓글을 달았습니다.";
		
		String url = "/fundTarget/detail/" + fundBoardTarget.getId();
		
		Alert alert = new Alert();
		alert.setContent(content);
		alert.setUrl(url);
		alert.setHostUser(fundBoardTarget.getFundUser());
		alert.setGuestUser(user.get());
		alert.setFundBoardTarget(fundBoardTarget);
		
		alertRepository.save(alert);
	}
	

	
	//미지정펀딩에 댓글 생성시 알림 등록
	public void answerAlertBoard(FundBoard fundBoard, Principal principal) {
		Optional<FundUser> user = fundUserService.findByuserName(principal.getName());
		if(user.isEmpty()) {
			Optional<FundArtist> artist = fundArtistService.findByuserName(principal.getName());
			
			String content = "[" + fundBoard.getSubject() + "] 글에 (" + artist.get().getNickname() +
					")님이 댓글을 달았습니다.";
			
			String url = "/fundBoard/detail/" + fundBoard.getId();
			
			Alert alert = new Alert();
			alert.setContent(content);
			alert.setUrl(url);
			alert.setHostUser(fundBoard.getFundUser());
			alert.setGuestArtist(artist.get());
			alert.setFundBoard(fundBoard);
			
			alertRepository.save(alert);
			
			return;
		}
	
		String content = "[" +fundBoard.getSubject() + "]글에 (" + user.get().getNickname() +
				")님이 댓글을 달았습니다.";
		
		String url = "/fundBoard/detail/" + fundBoard.getId();
		
		Alert alert = new Alert();
		alert.setContent(content);
		alert.setUrl(url);
		alert.setHostUser(fundBoard.getFundUser());
		alert.setGuestUser(user.get());
		alert.setFundBoard(fundBoard);
		
		alertRepository.save(alert);
	}

	
	public List<Alert> findByHostUser(FundUser user){
		List<Alert> aList = alertRepository.findByHostUser(user);
		return aList;
	}
		
}
