package com.jinsite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinsite.config.jinSiteMockUser;
import com.jinsite.domain.Comment;
import com.jinsite.domain.Post;
import com.jinsite.domain.User;
import com.jinsite.repository.UserRepository;
import com.jinsite.repository.comment.CommentRepository;
import com.jinsite.repository.post.PostRepository;
import com.jinsite.request.PostCreate;
import com.jinsite.request.PostEdit;
import com.jinsite.request.comment.CommentCreate;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.result.ModelResultMatchers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@WebMvcTest -> @SpringBootTest로 변경
//@WebMvcTest에서는 간단한 컨트롤러 웹 레이어 테스트를 할때는 괜찮은데 지금은 서비스도 만들고 레파지토리도 만들었기 때문이다 => 에플리케이션 전반적인 테스트를 하기 때문 => mockMvc주입이 안됨!! => @AutoConfigureMockMvc 사용하자.
@AutoConfigureMockMvc
@SpringBootTest
class CommentControllerTest {

    //json 프로세싱해주는 라이브러리
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("댓글 작성")
    void test2() throws Exception {
        //given
        User user = User.builder()
                .name("진사이트")
                .email("jin@gmail.com")
                .email("1234")
                .build();
        userRepository.save(user);

        Post post = Post.builder()
                .title("1234567890987654321")
                .content("bar")
                .user(user)
                .build();
        postRepository.save(post);

        CommentCreate request = CommentCreate.builder()
                .author("호순이")
                .password("1234567")
                .content("댓글입니다lolololol.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts/{postsId}/comments", post.getId())
                        .contentType(APPLICATION_JSON_VALUE)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        //서비스 테스트 만들기 일단 귀찮으니 여기서 해결
        Assertions.assertEquals(1L, commentRepository.count());

        Comment savedComment = commentRepository.findAll().get(0);
        assertEquals("호순이", savedComment.getAuthor());
        assertNotEquals("1234567", savedComment.getPassword());
        assertTrue(passwordEncoder.matches("1234567", savedComment.getPassword()));
        assertEquals("댓글입니다lolololol.", savedComment.getContent());

    }
}