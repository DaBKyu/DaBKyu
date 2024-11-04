package com.dabkyu.dabkyu.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.dabkyu.dabkyu.dto.MemberOAuth2DTO;
import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;

@Service
@AllArgsConstructor
@Log4j2
public class OAuth2UserDetailsServiceImpl extends DefaultOAuth2UserService {

    private final PasswordEncoder pwdEncoder;
    private final HttpSession session;
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

        log.info("----------OAuth2 로그인 단계 : Provider 정보 확보({})----------", provider);
		log.info("----------OAuth2 로그인 단계 : ProviderID 정보 확보({})----------", providerId);
		log.info("----------OAuth2 로그인 단계 : email 정보 확보({})----------", email);

        oAuth2User.getAttributes().forEach((k, v) -> {
            log.info(k + ": " + v);
        });

        // 회원 정보 가져오기(첫 로그인 시 정보 저장)
        MemberEntity member = saveSocialMember(email,provider);

        // Role값을 가져와 SimpleGrantedAuthority 객체(사용자 Role값을 받는 객체)에 저장.
        // SimpleGrantedAuthority 리스트에 저장.
        List<SimpleGrantedAuthority> grantedAuthorities = new ArrayList<>();
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(member.getRole());
        grantedAuthorities.add(grantedAuthority);

        log.info("----------OAuth2 로그인 단계 : Role 설정----------");

        // attributes, authorities, name을 memberOAuth2DTO에 저장.
        MemberOAuth2DTO memberOAuth2DTO = new MemberOAuth2DTO();

        memberOAuth2DTO.setAttribute(oAuth2User.getAttributes());
        memberOAuth2DTO.setAuthorities(grantedAuthorities);
        memberOAuth2DTO.setName(member.getUsername());
        
        // 세션 설정
        session.setAttribute("email", email);
        session.setAttribute("username", member.getUsername());
        session.setAttribute("role", member.getRole());
        session.setAttribute("FromSocial", member.getFromSocial());

        log.info("----------OAuth2 로그인 단계 : 세션 설정----------");
        log.info("----------세션 email : {}", (String)session.getAttribute("email"));
        log.info("----------세션 username : {}", (String)session.getAttribute("username"));
        log.info("----------세션 FromSocial : {}", (String)session.getAttribute("FromSocial"));
        
        return memberOAuth2DTO;
    }

    private MemberEntity saveSocialMember(String email, String provider) {
        // 구글 회원 계정으로 로그인 한 회원 정보를 입력받기

        // 기존 회원이 SNS로그인 할 겨우 기존 회원 정보를 리턴
        Optional<MemberEntity> result = memberRepository.findById(email);
        if (result.isPresent()) {
            return result.get();
        }

        // 타기관으로부터 정보를 넘겨받고 추가 입력 창에서 입력 받을 지 상의 필요.
        MemberEntity member = MemberEntity.builder()
                                          .email(email)
                                          .username(provider.toUpperCase() + "회원")
                                          .password(pwdEncoder.encode("12345"))
                                          .role("USER")
                                          .regdate(LocalDateTime.now())
                                          .fromSocial("Y")
                                          .build();
        memberRepository.save(member);
        return member;
    }
}
