package com.funding.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.funding.fundArtist.FundArtist;
import com.funding.fundArtist.FundArtistRepository;
import com.funding.fundUser.FundUser;
import com.funding.fundUser.FundUserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final FundUserRepository fundUserRepository;
    private final FundArtistRepository fundArtistRepository;

    @Override 
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<FundUser> _fundUser = this.fundUserRepository.findByusername(username);
        if (_fundUser.isEmpty()) {
        	Optional<FundArtist> _fundArtist = this.fundArtistRepository.findByusername(username);
        	
        	if(_fundArtist.isEmpty()) {
        		throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        	}
        	
            FundArtist fundArtist = _fundArtist.get();
            List<GrantedAuthority> authorities = new ArrayList<>();
            if ("admin".equals(username)) {
                authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
            }else if(fundArtist.getRole().equals("artist")) {
                authorities.add(new SimpleGrantedAuthority(UserRole.ARTIST.getValue()));
            }else {
                authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
            }
            System.out.println("@@@@@@@@@@@ 아티스트 접속 성공");
            return new User(fundArtist.getUsername(), fundArtist.getPassword(), authorities);
        }
        
        FundUser fundUser = _fundUser.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(username)) {
            authorities.add(new SimpleGrantedAuthority(UserRole.ADMIN.getValue()));
        }else if(fundUser.getRole().equals("user")) {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }else {
            authorities.add(new SimpleGrantedAuthority(UserRole.USER.getValue()));
        }
        System.out.println("@@@@@@@@@@@ 유저 접속 성공");
        return new User(fundUser.getUsername(), fundUser.getPassword(), authorities);
    }
}