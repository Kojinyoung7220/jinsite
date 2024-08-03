package com.jinsite.service;

import com.jinsite.crypto.ScryptPasswordEncoder;
import com.jinsite.domain.User;
import com.jinsite.exception.AlreadyExistsEmailException;
import com.jinsite.exception.InvalidSingInInformation;
import com.jinsite.repository.UserRepository;
import com.jinsite.request.Login;
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
        assertEquals("1234", user.getPassword());
        assertEquals("영진", user.getName());
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

    @Test
    @DisplayName("로그인 성공")
    void test3(){
        //given
        User user1 = User.builder()
                .email("jin@gmail.com")
                .password("1234")
                .name("영진")
                .build();
        userRepository.save(user1);


        Login login = Login.builder()
                .email("jin@gmail.com")
                .password("1234")
                .build();

        //when
        Long userId = authService.signIn(login);

        //then
        Assertions.assertNotNull(userId);
    }
    @Test
    @DisplayName("비밀번호 틀림.")
    void test4(){
        //given
        Signup signup = Signup.builder()
                .password("1234")
                .email("jin@gmail.com")
                .name("영진")
                .build();
        authService.signup(signup);

        Login login = Login.builder()
                .email("jin@gmail.com")
                .password("678")
                .build();

        //expected
        Assertions.assertThrows(InvalidSingInInformation.class,() -> authService.signIn(login));
    }

}