package com.funding.user.mailValidation;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class EmailController {
	
	private final EmailService emailService;
	
	@RequestMapping("/emailConfirm")
	public String emailConfirm() throws Exception {

		
 	  String confirm = emailService.sendSimpleMessage("zlwmdnlsl86@naver.com");

	  return confirm;
	}
	
}
