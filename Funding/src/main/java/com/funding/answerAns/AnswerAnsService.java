package com.funding.answerAns;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.funding.fundUser.FundUser;
import com.funding.answer.Answer;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AnswerAnsService {

	private final AnswerAnsRepository answerAnsRepository;
	
	
	//답변 1개 가져오기
	public AnswerAns findById(Integer id) {
		Optional<AnswerAns> answerAns = answerAnsRepository.findById(id);
		return answerAns.get();
	}
	
	
	
	//댓글의 답변 찾아오기
	public List<AnswerAns> findByAnswer(Answer answer){
		List<AnswerAns> answerAnsList = answerAnsRepository.findByanswerId(answer);
		return answerAnsList;
	}
	
	//답변 생성
	public void createAnswerAns(String content, Answer answer, FundUser fundUser) {
		AnswerAns answerAns = new AnswerAns();
		answerAns.setContent(content);
		answerAns.setCreateDate(LocalDateTime.now());
		answerAns.setFundUserId(fundUser);
		answerAns.setAnswerId(answer);
		
		answerAnsRepository.save(answerAns);
	}
	
	//답변 삭제
	public void deleteAnswerAns(AnswerAns answerAns) {
		answerAnsRepository.delete(answerAns);
	}
	
	
	
};
