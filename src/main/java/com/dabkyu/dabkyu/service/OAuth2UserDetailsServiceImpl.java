package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.dabkyu.dabkyu.dto.MemberOAuth2DTO;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;

@Service
@AllArgsConstructor
@Log4j2
public class OAuth2UserDetailsServiceImpl extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 타기관의 로그인 응답 메시지가 담긴 OAth2UserRequest를
        // super.loadUser(userRequest)를 통해 OAuth2User에 입력.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        String providerId = oAuth2User.getAttribute("sub");
        String email = null;

        // 카카오 추가 상의 필요
        if (provider.equals("naver")) {
            Map<String, Object> response = oAuth2User.getAttribute("response");
            email = (String) response.get("email");
        } else if (provider.equals("google")) {
            email = oAuth2User.getAttribute("email");
        }

        log.info("----------OAuth2 로그인 단계 : Provider 정보 ({})----------", provider);
		log.info("----------OAuth2 로그인 단계 : ProviderID 정보({})----------", providerId);
		log.info("----------OAuth2 로그인 단계 : email 정보 ({})----------", email);

        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info(k + ": " + v);
        });

        // 기존 회원이 SNS로그인 할 경우 기존 회원 정보를 리턴
        Optional<MemberEntity> result = memberRepository.findById(email);

        // attributes, authorities, name을 memberOAuth2DTO에 저장.
        MemberOAuth2DTO memberOAuth2DTO = new MemberOAuth2DTO();

        memberOAuth2DTO.setAttribute(oAuth2User.getAttributes());
        memberOAuth2DTO.setName(email);
        memberOAuth2DTO.setAuthorities(
            result.isPresent()?
            Collections.singletonList(new SimpleGrantedAuthority(result.get().getRole())):
            Collections.singletonList(new SimpleGrantedAuthority("TEMP"))
        );
        
        return memberOAuth2DTO;
    }

}
