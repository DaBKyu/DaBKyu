package com.dabkyu.dabkyu;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(

        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception

    ) throws IOException, ServletException {

        log.info("-------------- OAuth2 로그인 실패 --------------");

        setDefaultFailureUrl("/member/login");
        super.onAuthenticationFailure(request, response, exception);
    }
}
