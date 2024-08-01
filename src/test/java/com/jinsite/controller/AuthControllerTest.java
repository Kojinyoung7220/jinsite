package com.jinsite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinsite.domain.Session;
import com.jinsite.domain.User;
import com.jinsite.repository.PostRepository;
import com.jinsite.repository.SessionRepository;
import com.jinsite.repository.UserRepository;
import com.jinsite.request.Login;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }
    @Test
    @DisplayName("로그인 정상응답.")
    void test1() throws Exception {
        //given
        userRepository.save(User.builder()
                .name("진사이트")
                .email("jin@gmail.com")
                .password("1234") //Scrypt, Bcrypt 로 비밀번호 암호화할 수 있음.
                .build());

        Login login = Login.builder()
                .email("jin@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션 1개 생성.")
    void test2() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("진사이트")
                .email("jin@gmail.com")
                .password("1234") //Scrypt, Bcrypt 로 비밀번호 암호화할 수 있음.
                .build());

        Login login = Login.builder()
                .email("jin@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());
        //트랜잭션을 test에 붙이다 보니 저장을안하고 그냥
        //user.getSessions().size()이렇게 해도 조회가됨.
//        User loggedInUser = userRepository.findById(user.getId())
//                        .orElseThrow(RuntimeException::new);
        assertEquals(1L, user.getSessions().size());
    }

    @Test
    @DisplayName("로그인 성공 후 세션 응답.")
    void test3() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("진사이트")
                .email("jin@gmail.com")
                .password("1234") //Scrypt, Bcrypt 로 비밀번호 암호화할 수 있음.
                .build());

        Login login = Login.builder()
                .email("jin@gmail.com")
                .password("1234")
                .build();

        String json = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken", Matchers.notNullValue()))
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다. /foo")
    void test4() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("진사이트")
                .email("jin@gmail.com")
                .password("1234") //Scrypt, Bcrypt 로 비밀번호 암호화할 수 있음.
                .build());
        Session session = user.addSession();
        userRepository.save(user);


        //expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    void test5() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .name("진사이트")
                .email("jin@gmail.com")
                .password("1234") //Scrypt, Bcrypt 로 비밀번호 암호화할 수 있음.
                .build());
        Session session = user.addSession();
        userRepository.save(user);


        //expected
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken() +"-other-")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());

    }
}
