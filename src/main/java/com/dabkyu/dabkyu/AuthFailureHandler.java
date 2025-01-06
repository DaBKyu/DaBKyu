package com.dabkyu.dabkyu;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@AllArgsConstructor
@Log4j2
public class AuthFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(
        
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception

    ) throws IOException, ServletException {
        log.info("-------------로그인 실패-------------");
        setDefaultFailureUrl("/member/login");

        super.onAuthenticationFailure(request, response, exception);
    }
}
