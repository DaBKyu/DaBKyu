package com.dabkyu.dabkyu;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(

        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication

    ) throws IOException, ServletException {

        log.info("--------------  OAuth2 로그인 성공 --------------");

        setDefaultTargetUrl("/shop/main");
        super.onAuthenticationSuccess(request, response, authentication);
    }
}
