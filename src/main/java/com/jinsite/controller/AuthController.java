package com.jinsite.controller;

import com.jinsite.config.AppConfig;
import com.jinsite.request.Signup;
import com.jinsite.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AppConfig appConfig;

    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup){
        /// TODO: 2024-08-03  signup을 바로 넘겨주는건 안좋은 방법 => DTO를 통해 해결하는 게 좋다. 찾아보자.
        authService.signup(signup);
    }

}
