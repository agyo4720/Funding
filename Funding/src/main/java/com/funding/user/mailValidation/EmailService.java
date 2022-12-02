package com.funding.user.mailValidation;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {
	
	//의존성 주입
	private final JavaMailSender emailSender;
	private final SpringTemplateEngine templateEngine;
	
	//인증번호 생성
	private String ePw;	
	
	//랜덤 인증코드 만들기
	public void createKey() {
		Random random = new Random();
		StringBuffer key = new StringBuffer();
		
		for(int i=0; i<6; i++) {
			key.append(random.nextInt(10));
		}		
		
		ePw = key.toString();
	}

	

	
	
	//메일 양식 작성
	public MimeMessage createEmailForm(String email) throws MessagingException, UnsupportedEncodingException {
		log.info("####수신자: " + email);
		
		
		createKey(); //인증 코드 생성
		log.info("####인증번호: " + ePw);
		
		String setFrom = "qor8001@naver.com";
		String toEmail = email; //받는 사람
		String title = "Let's Funding 이메일 인증"; //메일 제목
		

		MimeMessage message = emailSender.createMimeMessage();
		
		message.addRecipients(MimeMessage.RecipientType.TO, email); //to: 보내는 대상
		message.setSubject(title); //메일 제목
		message.setFrom(setFrom);
		message.setText(setContext(ePw), "utf-8", "html");
		 
		
        return message;

    
	}
	

	
	//메일 발송
	public String sendEmail(String toEmail) throws MessagingException, UnsupportedEncodingException {
		
		MimeMessage emailForm = createEmailForm(toEmail); //message 객체 안에 내가 전송할 메일의 내용 담기
		
		try {
			emailSender.send(emailForm); //bean으로 등록해둔 javaMailSender 객체를 사용하여 이메일을 보냄
			
		}catch(MailException es) {
			es.printStackTrace();
			throw new IllegalArgumentException();
		}
		
		return ePw; //메일로 보냈던 인증 코드를 서버로 리턴
	}
	
	
	//타임리프를 이용한 context 설정
	public String setContext(String code) {
		Context context = new Context();
		context.setVariable("code", code);
		return templateEngine.process("/emailAuth/mail", context);
	}
	

	
	
}
