package com.funding.answer;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundArtist.FundArtist;
import com.funding.fundBoard.FundBoard;
import com.funding.fundBoardTarget.FundBoardTarget;
import com.funding.fundUser.FundUser;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	
	//댓글 삭제
	public void deleteAnswer(Integer id) {
		Optional<Answer> answer = answerRepository.findById(id);
		answerRepository.delete(answer.get());
	}
	
	
	//댓글 id로 찾기
	public Answer findById(Integer id) {
		Optional<Answer> answer = answerRepository.findById(id);
		return answer.get();
	}
	
	//미지정 보드 댓글 만들기
	public void createBoardAnswerUser(String content, FundBoard fundBoard, FundUser user) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setFundBoard(fundBoard);
		answer.setFundUser(user);
		
		answerRepository.save(answer);
	}
	
	//미지정 보드 아티스트 댓글 만들기
	public void createBoardAnswerArt(String content, FundBoard fundBoard, FundArtist art) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setFundArtist(art);
		answer.setFundBoard(fundBoard);
		
		answerRepository.save(answer);
	}
	
	//지정 보드 유저 댓글 만들기
	public void createTargetAnswerUser(String content, FundBoardTarget fundBoardTarget, FundUser user) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setFundUser(user);
		answer.setFundBoardTarget(fundBoardTarget);
		
		answerRepository.save(answer);
	}
	
	//지정 보드 아티스트 댓글 만들기
	public void createTargetAnswerArt(String content, FundBoardTarget fundBoardTarget, FundArtist art) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setFundArtist(art);
		answer.setFundBoardTarget(fundBoardTarget);
		
		answerRepository.save(answer);
	}
	
	
	
	public List<Answer> findByFundBoardTarget(FundBoardTarget fundBoardTarget){
		List<Answer> aList = answerRepository.findByFundBoardTarget(fundBoardTarget);
		return aList;
	}
	
	public List<Answer> findByFundBoard(FundBoard fundBoard){
		List<Answer> answerList = this.answerRepository.findByFundBoard(fundBoard);
		return answerList;
	}
	
	
	
}
