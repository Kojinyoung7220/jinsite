package com.jinsite.service;

import com.jinsite.domain.Session;
import com.jinsite.domain.User;
import com.jinsite.exception.InvalidSingInInformation;
import com.jinsite.repository.UserRepository;
import com.jinsite.request.Login;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public String signIn(Login login){

        // DB에서 조회
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                .orElseThrow(InvalidSingInInformation::new);

        Session session = user.addSession();

        return session.getAccessToken();
    }
}
