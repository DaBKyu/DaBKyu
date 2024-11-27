package com.dabkyu.dabkyu;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dabkyu.dabkyu.service.VisitorService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class VisitorLogFilter extends OncePerRequestFilter {

    private final VisitorService visitorService;

    public VisitorLogFilter(VisitorService visitorService) {
        this.visitorService = visitorService;
    }

    @SuppressWarnings("null")
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String ipAddress = request.getRemoteAddr();
        visitorService.logVisitor(ipAddress); // 고유 방문자 기록
        filterChain.doFilter(request, response);
    }
}
