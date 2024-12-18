package com.dabkyu.dabkyu;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.util.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@AllArgsConstructor
@Log4j2
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberRepository memberRepository;
    private final JWTUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(

        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication

    ) throws IOException, ServletException {

		String email = authentication.getName();
		
		MemberEntity memberEntity = memberRepository.findById(email).get();		
		
		log.info("------------------------ OAuth2 토큰 및 쿠키 생성 ------------------------");
		
		Map<String, Object> payload = new HashMap<>();
		payload.put("email", email);
				
		String accessToken = (String)jwtUtil.generateToken(payload, 1);
		String refreshToken = (String)jwtUtil.generateToken(payload, 5);
		
		String SocialName = email.substring(email.lastIndexOf("@")).substring(1);
		log.info("------------------------ Social Name : {} ------------------------",SocialName);

		String url = "http://localhost:3000/shop/main"
				 + "&email=" + email 
				 + "&role=" + memberEntity.getRole()
				 + "&FromSocial=" + memberEntity.getFromSocial()
				 + "&username=" + URLEncoder.encode(memberEntity.getUsername(),"UTF-8")
				 + "&accessToken=" + accessToken
				 + "&refreshToken=" + refreshToken;
		
		log.info("------------------------ OAuth2 로그인 성공 ------------------------");
		
		setDefaultTargetUrl(url);		
		super.onAuthenticationSuccess(request, response, authentication);		
    }
}
