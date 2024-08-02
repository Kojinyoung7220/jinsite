package com.jinsite.controller;

import com.jinsite.request.Login;
import com.jinsite.response.SessionResponse;
import com.jinsite.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private static final String KEY = "3ZIagOxhx+NOvMS70FzYns7j2tjd07GO5yuyHgjFcds=";

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login){
        Long userId = authService.signIn(login);

        //인코딩한 키를 다시 디코딩해서 숨기기. 이때 사용자에 따라 시그니쳐 값이 계속 바뀜
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));

        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                .compact();

        return new SessionResponse(jws);

    }
}
