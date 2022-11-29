package com.funding.user;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistRepository;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

   private final FundUserRepository fundUserRepository;
   private final FundArtistRepository fundArtistRepository;

   @Override
   public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse response,
         Authentication authentication) throws IOException, ServletException {
	   
	   Optional<FundUser> FU = fundUserRepository.findByusername(authentication.getName());
	   if(FU.isPresent()) {
		   httpServletRequest.getSession().setAttribute("myInfo", FU.get());
	   }
	   
	   Optional<FundArtist> FA = fundArtistRepository.findByusername(authentication.getName());
       if(FA.isPresent()) {
    	  httpServletRequest.getSession().setAttribute("myInfo", FA.get());
       }
      
      response.sendRedirect("/");

   }
}