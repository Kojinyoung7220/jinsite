package com.jinsite.service;

import com.jinsite.crypto.PasswordEncoder;
import com.jinsite.crypto.ScryptPasswordEncoder;
import com.jinsite.domain.User;
import com.jinsite.exception.AlreadyExistsEmailException;
import com.jinsite.exception.InvalidSingInInformation;
import com.jinsite.repository.UserRepository;
import com.jinsite.request.Login;
import com.jinsite.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long signIn(Login login){

        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSingInInformation::new);

        //평문 페스워드와 암호화된 페스워드가 일치하는지 확인해주는 메서드이다.
        if (!passwordEncoder.matches(login.getPassword(), user.getPassword())) {
            throw new InvalidSingInInformation();
        }

        return user.getId();
    }

    public void signup(Signup signup) {
        //중복 췌크~
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if(userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());

        var user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
