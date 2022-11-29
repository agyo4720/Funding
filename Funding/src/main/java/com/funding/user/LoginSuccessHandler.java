package com.funding.user;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

   private final FundUserRepository fundUserRepository;

   @Override
   public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response,
         Authentication authentication) throws IOException, ServletException {
	   
	   FundUser FU = fundUserRepository.findByusername(authentication.getName()).get();
       httpServletRequest.getSession().setAttribute("myInfo", FU);
      
      
      response.sendRedirect("/");

   }
}