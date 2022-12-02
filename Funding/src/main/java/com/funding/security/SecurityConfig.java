package com.funding.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.funding.user.LoginSuccessHandler;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	private final UserSecurityService userSecurityService;
	private final LoginSuccessHandler loginSuccessHandler;
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	// 페이지 접속권한 모두에게
        http.authorizeRequests().antMatchers("/**").permitAll()
        // 토근 비활성화 
        .and()
        .csrf().ignoringAntMatchers("/**")
        // H2 접속시 화면 깨짐 방지
        .and()
        .headers()
        .addHeaderWriter(new XFrameOptionsHeaderWriter(
                XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))
        // 로그인 주소
        .and()
        .formLogin()
        .loginPage("/user/login")
        .defaultSuccessUrl("/")
        .successHandler(loginSuccessHandler)
        
        // 로그아웃
        .and()
        .logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true)
                ;
        
        
        return http.build();
    }
        
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    
}
