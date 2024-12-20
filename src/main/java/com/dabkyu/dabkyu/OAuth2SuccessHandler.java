package com.dabkyu.dabkyu;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.dabkyu.dabkyu.entity.MemberEntity;
import com.dabkyu.dabkyu.entity.repository.MemberRepository;
import com.dabkyu.dabkyu.util.JWTUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
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
		Optional<MemberEntity> memberOpt = memberRepository.findById(email);

		String redirectUrl;
		
		log.info("------------------------ OAuth2 토큰 및 쿠키 생성 ------------------------");

		if (memberOpt.isPresent()) {
			// 기존 회원: 메인 페이지로 이동
			MemberEntity memeber = memberOpt.get();
			// 쿠키 생성
			setAuthCookies(response, email, memeber.getRole());
			// 메인페이지
			redirectUrl = "http://localhost:3000/shop/main";
		} else {
			// 새 회원: 추가 정보 입력 페이지로 이동
			setAuthCookies(response, email, "TEMP");
			redirectUrl = "http://localhost:3000/signup/additional-info";
		}
		// 리다이렉트
		response.sendRedirect(redirectUrl);
    }

    private void setAuthCookies(
		HttpServletResponse response,
		String email,
		String role
	) {
        // JWT 생성: 액세스 토큰과 리프레시 토큰 생성
        Map<String, Object> payload = new HashMap<>();
        payload.put("email", email); // 토큰에 이메일 포함
        payload.put("role", role);  // 토큰에 역할 포함

        String accessToken = jwtUtil.generateToken(payload, 60);	// 1시간 유효
        String refreshToken = jwtUtil.generateToken(payload, 7 * 24*60);	// 7일 유효

        // 쿠키 설정: 각 토큰 및 사용자 정보를 쿠키로 설정
        createCookie(response, "accessToken", accessToken, 3600); // 1시간 (3600초)
        createCookie(response, "refreshToken", refreshToken, 7 * 24 * 3600); // 7일
        createCookie(response, "email", email, 7 * 24 * 3600); // 이메일 정보 저장
        createCookie(response, "role", role, 7 * 24 * 3600); // 역할 정보 저장
        createCookie(response, "FromSocial", "Y", 7 * 24 * 3600); // 소셜 여부 저장
    }

	private void createCookie(
		HttpServletResponse response,
		String name,
		String value,
		int maxAge
	) {
		Cookie cookie = new Cookie(name, value);
		cookie.setHttpOnly(true);	// JavaScript에서 접근 불가
		// HTTPS에서만 전송
		// cookie.setSecure(true); 
        cookie.setPath("/");	// 모든 경로에서 접근 가능
        cookie.setMaxAge(maxAge);

        response.addCookie(cookie);

		String cookieHeader = String.format(
			// "%s=%s; Path=%s; Max-Age=%d; HttpOnly; Secure; SameSite=Lax",
			"%s=%s; Path=%s; Max-Age=%d; HttpOnly; SameSite=Lax",
            cookie.getName(),
			cookie.getValue(),
			cookie.getPath(),
			cookie.getMaxAge()
		);

		response.addHeader("Set-Cookie", cookieHeader);
	}
}
