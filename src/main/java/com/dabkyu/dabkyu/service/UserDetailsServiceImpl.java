package com.dabkyu.dabkyu.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        MemberEntity member = memberRepository.findById(username).get();//username은 로그인요청에서 가져온 userid
        
        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 아이디 입니다.");
        }

        // SimplegrantedAuthority: 사용자 Role값을 받는 객체(key, value 타입 map)
        // Role값을 가져와 저장
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole());
        grantedAuthorities.add(grantedAuthority);

        // User 객체에 username, password, role값 넣어 저장
        User user = new User(username, member.getPassword(), grantedAuthorities);

        return user;
    }
}
