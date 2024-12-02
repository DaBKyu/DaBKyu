package com.dabkyu.dabkyu;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.dabkyu.dabkyu.service.UserDetailsServiceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
@Log4j2
public class WebSecurityConfig {

    private final AuthSuccessHandler authSuccessHandler;
    private final AuthFailureHandler authFailureHandler;
    private final UserDetailsServiceImpl userDetailsService;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final OAuth2FailureHandler oAuth2FailureHandler;

    //스프링 시큐리티의 암호화 객체를 빈에 등록
    @Bean
    public BCryptPasswordEncoder pwdEncoder() {
        return new BCryptPasswordEncoder();
    }

    //스프링 시큐리티 적용 제외 대상 설정을 빈에 등록
    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/images/**", "/css/**", "/js/**");
    }

    //스프링 시큐리티 필터 빈에 등록
    @Bean
    public SecurityFilterChain filter(HttpSecurity http) throws Exception {

        //X-Frame-Options 허용
        http
        .headers(headers -> headers
            .frameOptions(frame -> frame.sameOrigin()) // iframe을 같은 출처에서 허용
        ); 

        //formLogin 설정
        http.formLogin(
            (login) -> login
            .usernameParameter("email")
            .loginPage("/member/login")
            .successHandler(authSuccessHandler)
            .failureHandler(authFailureHandler)
        );
        
        //자동로그인 설정
        http.rememberMe(
            (me) -> me
            .key("dabkyu")
            .alwaysRemember(false)
            .tokenValiditySeconds(3600*24*7)
            .rememberMeParameter("remember-me")
            .userDetailsService(userDetailsService)
            .authenticationSuccessHandler(authSuccessHandler)
        );
        
        //OAuth2 설정
        http.oauth2Login(
            (login) -> login
            .loginPage("/member/login")
            .successHandler(oAuth2SuccessHandler)
            .failureHandler(oAuth2FailureHandler)
        );

        //접근권한 설정(Access Control)
        http.authorizeHttpRequests(
            (authz) -> authz
            .requestMatchers("/member/**").permitAll()
            .requestMatchers("/mypage/**").hasAnyAuthority("USER", "MASTER")
            .requestMatchers("/shop/**").permitAll()
            .requestMatchers("/purchase/**").hasAnyAuthority("USER", "MASTER")
            .requestMatchers("/master/**").hasAnyAuthority("MASTER")
            .anyRequest().authenticated()
        );

        //세션 설정
        http.sessionManagement(
            (management) -> management
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .expiredUrl("/member/login")
        );

        //로그아웃
        http.logout(
            (logout) -> logout
            .logoutUrl("/member/logout")
            .logoutSuccessUrl("/member/login")
            .invalidateHttpSession(true)
            .deleteCookies("JSESSIONID", "remember-me")
            .permitAll()
        );

        //CSRF, CORS 보안 설정 비활성화
        http.csrf((csrf) -> csrf.disable());
        http.cors((cors) -> cors.disable());

        log.info("------------스프링 시큐리티 설정 완료------------");

        return http.build();
    }
}
