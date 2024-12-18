package com.dabkyu.dabkyu.service;

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

        //username은 로그인요청에서 가져온 userid
        
        MemberEntity memberEntity = memberRepository.findById(username)
                                                                                        .orElseThrow(()-> new UsernameNotFoundException("아이디가 존재하지 않습니다."));

        return new UserDetailsImpl(memberEntity);
    }
}
