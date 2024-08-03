package com.jinsite.config;

import com.jinsite.config.data.UserSession;
import com.jinsite.domain.Session;
import com.jinsite.exception.Unauthorized;
import com.jinsite.repository.SessionRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Base64;
import java.util.Optional;

import static com.jinsite.domain.QSession.session;

/**
 * 아규먼트 리졸버 사용해서 로그인 처리
 * 세션을 통해 로그인을 처리 했지만 이제는 쿠키로 변환하였음.
 */
@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {
    //ex) foo 요청에 대한 라우터가 넘어왔을 때 이 포스트 컨트롤러가 너가 정말 원하는 DTO야?
    //ex) /foo로 왔을 때 이 메소드의 이 타입이 너가 원하는 게 맞아?

    private final SessionRepository sessionRepository;
    private final AppConfig appConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    //dto에 값을 세팅해준다
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String jws = webRequest.getHeader("Authorization");
        if(jws == null || jws.equals("")){
            throw new Unauthorized();
        }
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(appConfig.getJwtKey())
                    .build()
                    //이 과정에서 만료 여부도 자동으로 체크된다.
                    .parseClaimsJws(jws);
            String userId = claims.getBody().getSubject();
            return new UserSession(Long.parseLong(userId));

        } catch (JwtException e) {
            throw new Unauthorized();
        }
    }
}
