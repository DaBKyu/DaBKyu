package com.dabkyu.dabkyu;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.dabkyu.dabkyu.service.UserDetailsServiceImpl;
import com.dabkyu.dabkyu.util.JWTUtil;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@Log4j2
public class WebSecurityConfig {

    private final UserDetailsServiceImpl userDetailsService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;
    private final JWTUtil jwtUtil;

    // 스프링 시큐리티의 암호화 객체를 빈에 등록
    @Bean
    BCryptPasswordEncoder pwdEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 스프링 시큐리티 적용 제외 대상 설정을 빈에 등록
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/css/**", "/js/**", "/product/images/**", "/product/thumbnails/**");
    }

    // 스프링 시큐리티 필터 빈에 등록
    @Bean
    SecurityFilterChain filter(HttpSecurity http) throws Exception {

        // X-Frame-Options 허용
        http
        .headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin()) // iframe을 같은 출처에서 허용
        ); 

        // formLogin 설정
        http.formLogin(
            (login) -> login.disable()
        );
        
        // OAuth2 설정
        http.oauth2Login(
            (login) -> login.successHandler(oAuth2SuccessHandler)
                                    .failureHandler(oAuth2FailureHandler)
        );

        // JWT Filter 설정
        http.addFilterBefore(
            new JwtAuthFilter(userDetailsService, jwtUtil),
            UsernamePasswordAuthenticationFilter.class
        );

        // 접근권한 설정(Access Control)
        http.authorizeHttpRequests(
            (authz) -> authz.requestMatchers("/").permitAll()
                                        .requestMatchers("/member/**").permitAll()
                                        .requestMatchers("/mypage/**").hasAnyAuthority("USER", "MASTER", "TEMP")
                                        .requestMatchers("/shop/**").permitAll()
                                        .requestMatchers("/purchase/**").hasAnyAuthority("USER", "MASTER")
                                        .requestMatchers("/master/**").hasAnyAuthority("MASTER")
                                        .anyRequest().authenticated()
        );

        //세션 관리 비활성화(Stateless)
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //
        http.headers((headers-> headers
                                    .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))));


        //

        //CSRF, CORS 보안 설정 비활성화
        http.csrf((csrf) -> csrf.disable());
        http.cors((cors) -> cors.disable());

        log.info("------------스프링 시큐리티 설정 완료------------");
        return http.build();
    }

    //react와 연동을 위한 cors 설정
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000","http://www.dabkyu.com","https://www.dabkyu.com")); // 허용할 출처
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 허용할 HTTP 메서드
        configuration.setAllowedHeaders(List.of("*")); // 허용할 헤더
        configuration.setAllowCredentials(true); // 자격 증명 전송 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 엔드포인트에 대해 CORS 허용

        return source;
    }
    
	@Bean
    AuthenticationManager authenticationManager
    	(AuthenticationConfiguration configuration) throws Exception{
		return configuration.getAuthenticationManager(); 
	}

    @SuppressWarnings("removal")
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers.frameOptions().disable()) // X-Frame-Options 비활성화
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // 모든 요청 허용 (개발용)
        return http.build();
    }
}

