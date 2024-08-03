package com.jinsite.controller;

import com.jinsite.config.AppConfig;
import com.jinsite.request.Login;
import com.jinsite.request.Signup;
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
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;


    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login){
        Long userId = authService.signIn(login);

        //인코딩한 키를 다시 디코딩해서 숨기기. 이때 사용자에 따라 시그니쳐 값이 계속 바뀜
        SecretKey key = Keys.hmacShaKeyFor(appConfig.getJwtKey());

        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(key)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 60000)) // 1분 후 만료
                .compact();
        return new SessionResponse(jws);
    }

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup){
        /// TODO: 2024-08-03  signup을 바로 넘겨주는건 안좋은 방법 => DTO를 통해 해결하는 게 좋다. 찾아보자.
        authService.signup(signup);
    }

}
