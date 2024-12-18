package com.dabkyu.dabkyu;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dabkyu.dabkyu.service.UserDetailsServiceImpl;
import com.dabkyu.dabkyu.util.JWTUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

	private final UserDetailsServiceImpl userDetailsServiceImpl;
	private final JWTUtil jwtUtil;
	
	@Override
	protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
		
		// REST API 실행 시 HTTP Request 헤더에 포함된 Authorization 값 읽음
		String authorizationHeader = request.getHeader("Authorization");
		
		String email = "";
		
		// Authorization 내에 Bearer 필드가 존재하면 토큰값을 읽음
		if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {			
			String token = authorizationHeader.substring(7);			
			if(jwtUtil.validateToken(token).equals("VALID_JWT")) {
				try {
					// 토큰이 유효하면 토큰내의 payload에서 email을 읽음
					email = (String) jwtUtil.getDataFromToken(token).get("email");
					} catch(Exception e) {
						e.printStackTrace();
				}
		
				// 토큰에서 읽어 온 email로 사용자 정보를 읽어 와서 userDetails에 저장
				UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(email);
		
				if(userDetails != null) {					
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					// UsernamePasswordAuthenticationFilter를 통해 아이디, 패스워드 검증
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
				}
			}
		}
		filterChain.doFilter(request, response);		
	}
}
