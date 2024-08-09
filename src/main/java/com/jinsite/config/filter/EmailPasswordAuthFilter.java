package com.jinsite.config.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

/**
 * EmailPasswordAuthFilter는 이메일과 비밀번호를 사용하여 인증을 처리하는 커스텀 필터입니다.
 * 스프링 시큐리티의 인증 흐름에서 로그인 요청을 가로채고,
 * 인증 토큰을 생성하여 인증 매니저(AuthenticationManager)에게 전달하는 역할을 합니다.
 *
 * 이 필터는 AbstractAuthenticationProcessingFilter를 확장하여,
 * 지정된 로그인 URL로의 POST 요청을 처리합니다.
 * 사용자가 로그인 폼에 입력한 이메일과 비밀번호를 받아 인증을 시도합니다.
 */
public class EmailPasswordAuthFilter extends AbstractAuthenticationProcessingFilter {

    private final ObjectMapper objectMapper;
    /**
     * EmailPasswordAuthFilter 생성자
     *
     * @param loginUrl 인증 요청을 처리할 URL을 지정합니다.
     *                이 URL로의 POST 요청이 들어오면 이 필터가 동작하게 됩니다.
     * @param objectMapper JSON 데이터를 Java 객체로 변환하기 위해 사용됩니다.
     */
    public EmailPasswordAuthFilter(String loginUrl, ObjectMapper objectMapper) {
        super(loginUrl);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        EmailPassword emailPassword = objectMapper.readValue(request.getInputStream(), EmailPassword.class);

        UsernamePasswordAuthenticationToken token = UsernamePasswordAuthenticationToken.unauthenticated(
                emailPassword.email,
                emailPassword.password
        );

        token.setDetails(this.authenticationDetailsSource.buildDetails(request));    // 추가적인 인증 세부 정보를 설정합니다.
        return this.getAuthenticationManager().authenticate(token);     // 인증 매니저에게 토큰을 전달하여 실제 인증을 시도합니다.

    }
    /**
     * EmailPassword 클래스는 로그인 요청에서 전달된 이메일과 비밀번호를
     * 담기 위한 DTO(Data Transfer Object)입니다.
     *
     * 이 클래스는 로그인 폼의 데이터를 매핑하기 위해 사용됩니다.
     */
    @Getter
    private static class EmailPassword{
        private String email;
        private String password;
    }
}
