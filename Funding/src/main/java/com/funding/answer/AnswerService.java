package com.funding.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundUser.FundUser;
import com.funding.fundBoard.FundBoard;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerService {
	
	private final AnswerRepository answerRepository;
	
	
	//댓글 삭제
	public void deleteAnswer(Answer a) {
		answerRepository.delete(a);
	}
	
	
	//댓글 id로 찾기
	public Answer findById(Integer id) {
		Optional<Answer> answer = answerRepository.findById(id);
		return answer.get();
	}
	
	//댓글 만들기
	public void createAnswer(String content, FundBoard fundBoard, FundUser fundUser) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setCreateDate(LocalDateTime.now());
		answer.setFundBoard(fundBoard);
		answer.setFundUserId(fundUser);
		
		answerRepository.save(answer);
	}
	
	
	
	
}
