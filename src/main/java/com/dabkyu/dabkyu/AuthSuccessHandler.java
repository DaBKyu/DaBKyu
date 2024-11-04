package com.dabkyu.dabkyu;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.dabkyu.dabkyu.dto.MemberDTO;
import com.dabkyu.dabkyu.service.MemberService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Component
@AllArgsConstructor
@Log4j2
public class AuthSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final MemberService service;

    //로그인 성공시 실행될 명령문
    @Override
    public void onAuthenticationSuccess(

        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
        
    ) throws IOException, ServletException {

        // 로그인 시 입력된 userid
        MemberDTO member = service.memberInfo(authentication.getName());

        // 로그인 날짜 기록
        service.lastdateUpdate(member.getEmail(), "login");

        // 세션 생성
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(3600*24*7);
        session.setAttribute("email", member.getEmail());
        session.setAttribute("username", member.getUsername());
        session.setAttribute("role", member.getRole());
        session.setAttribute("fromSocial", member.getFromSocial());

        // 마지막 패스워드 변경 후 30일이 경과 되었을 경우
        LocalDateTime lastpwDate = member.getLastpwDate();
        String reDate = lastpwDate.plusDays(30).toString();
        String today = LocalDateTime.now().toString();

        String url = "/shop/list?page=1";

        if (reDate.compareTo(today) < 0) {
            url = "/member/checkPasswordNotice";
        }

        setDefaultTargetUrl(url);
        super.onAuthenticationSuccess(request, response, authentication);
        
    }

}
