package com.jinsite.config.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

////401오류 발생시켜준다 -> 로그인이 필요한 페이지인데
// 로그인이 안된 상태에서 접근을 했을때 로그인을 요청하게 해준다.

@Slf4j
public class Http401Handler implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        log.info("[인증오류] 로그인이 필요합니다.");
    }
}
