package com.dabkyu.dabkyu;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

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
        return (web) -> web.ignoring().requestMatchers("/images/**", "/css/**", "/js/**", "/product/images/**", "/product/thumbnails/**","/question/images/**",
        "/review/images/**","/mail/images/**"
    );
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
            .requestMatchers("/").permitAll()
            .requestMatchers("/member/**").permitAll()
            .requestMatchers("/mypage/**").hasAnyAuthority("USER", "MASTER")
            .requestMatchers("/shop/**").permitAll()
            .requestMatchers("/purchase/**").hasAnyAuthority("USER", "MASTER")
            //.requestMatchers("/master/**").hasAnyAuthority("MASTER")
            .requestMatchers("/master/**").permitAll()
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

        //
        http.headers((headers-> headers
                                    .addHeaderWriter(new XFrameOptionsHeaderWriter(
                                        XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN))));


        //

        //CSRF, CORS 보안 설정 비활성화
        http.csrf((csrf) -> csrf.disable());
        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        log.info("------------스프링 시큐리티 설정 완료------------");

        return http.build();
    }

    //react와 연동을 위한 cors 설정
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://www.dabkyu.com", "https://www.dabkyu.com"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }


    @SuppressWarnings("removal")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .headers(headers -> headers.frameOptions().disable()) // X-Frame-Options 비활성화
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll()); // 모든 요청 허용 (개발용)
        return http.build();
    }
}

