package com.jinsite.service;

import com.jinsite.domain.User;
import com.jinsite.exception.AlreadyExistsEmailException;
import com.jinsite.repository.UserRepository;
import com.jinsite.request.Signup;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @AfterEach
    void clean(){
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("회원가입 성공")
    void test1(){
        //given
        Signup signup = Signup.builder()
                .password("1234")
                .email("jin@gmail.com")
                .name("영진")
                .build();

        //when
        authService.signup(signup);

        //then
        assertEquals(1, userRepository.count());

        User user = userRepository.findAll().iterator().next();

        assertEquals("jin@gmail.com", user.getEmail());
        assertNotNull(user.getPassword());
//        assertEquals("1234", user.getPassword());
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일 테스트")
    void test2(){
        //given
        User user1 = User.builder()
                .email("jin@gmail.com")
                .password("1234")
                .name("영진")
                .build();
        userRepository.save(user1);

        Signup signup = Signup.builder()
                .email("jin@gmail.com")
                .password("1234")
                .name("진진자라")
                .build();
        //expected
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));
    }
}