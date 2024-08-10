package com.jinsite.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinsite.config.jinSiteMockUser;
import com.jinsite.domain.Post;
import com.jinsite.domain.User;
import com.jinsite.repository.post.PostRepository;
import com.jinsite.repository.UserRepository;
import com.jinsite.request.post.PostCreate;
import com.jinsite.request.post.PostEdit;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//@WebMvcTest -> @SpringBootTest로 변경
//@WebMvcTest에서는 간단한 컨트롤러 웹 레이어 테스트를 할때는 괜찮은데 지금은 서비스도 만들고 레파지토리도 만들었기 때문이다 => 에플리케이션 전반적인 테스트를 하기 때문 => mockMvc주입이 안됨!! => @AutoConfigureMockMvc 사용하자.
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    //json 프로세싱해주는 라이브러리
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void clean() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }


    @Test
    @DisplayName("글 작성 요청시(/posts) title값은 필수다.")
    void test2() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        //{"title": ""}
                        //{"title": null}
                        .content(json)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }

    @Test
    @DisplayName("글 작성 요청시(/posts) DB에 값이 저장된다.")
    @jinSiteMockUser()
    void test3() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String json = objectMapper.writeValueAsString(request);

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        //{"title": ""}
                        //{"title": null}
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
        //then
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }


    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        User user = User.builder()
                .name("진사이트")
                .email("jin@gmail.com")
                .email("1234")
                .build();
        userRepository.save(user);
        //given
        //클라이언트 요구사항
        //ex)json응답에서 title값 길이를 최대 10글자로 해주세요.
        //Post entity <-> PostResponse class
        Post post = Post.builder()
                .title("1234567890987654321")
                .content("bar")
                .user(user)
                .build();

        postRepository.save(post);


        //expected
        mockMvc.perform(get("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());

    }

//    @Test
//    @DisplayName("글 여러개 조회")
//    void test5() throws Exception {
//        //given
//        Post post1 = postRepository.save(Post.builder()
//                .title("title_1")
//                .content("content_1")
//                .build());
//
//        Post post2 = postRepository.save(Post.builder()
//                .title("title_2")
//                .content("content_2")
//                .build());
//
//
//        //expected
//        mockMvc.perform(get("/posts")
//                        .contentType(APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", Matchers.is(2)))
//                .andExpect(jsonPath("$[0].id").value(post1.getId()))
//                .andExpect(jsonPath("$[0].title").value("title_1"))
//                .andExpect(jsonPath("$[0].content").value("content_1"))
//                .andExpect(jsonPath("$[1].id").value(post2.getId()))
//                .andExpect(jsonPath("$[1].title").value("title_2"))
//                .andExpect(jsonPath("$[1].content").value("content_2"))
//                .andDo(print());
//        /**
//         * {id:... , title: ...} => 단권조회시
//         * [ {id:... , title: ...}, {id:... , title: ...} ] => 여러개 조회시 -> list로 감쌌으니깐.
//         */
//    }

    @Test
    @DisplayName("글 여러개 조회22")
    void test5() throws Exception {
        User user = User.builder()
                .name("진사이트")
                .email("jin@gmail.com")
                .email("1234")
                .build();
        userRepository.save(user);

        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("진사이트 제목 " + i)
                            .content("신축아파트 " + i)
                            .user(user)
                            .build();
                }).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);


        //expected
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].title").value("진사이트 제목 19"))
                .andExpect(jsonPath("$[0].content").value("신축아파트 19"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
        //Math함수를 써서 회피...!!
    void test6() throws Exception {
        User user = User.builder()
                .name("진사이트")
                .email("jin@gmail.com")
                .email("1234")
                .build();
        userRepository.save(user);

        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("진사이트 제목 " + i)
                            .content("신축아파트 " + i)
                            .user(user)
                            .build();
                }).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);


        //expected
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", Matchers.is(10)))
                .andExpect(jsonPath("$[0].title").value("진사이트 제목 19"))
                .andExpect(jsonPath("$[0].content").value("신축아파트 19"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정.")
    @jinSiteMockUser()
    void test7() throws Exception {
        User user = userRepository.findAll().get(0);

        //given
        Post post = Post.builder()
                .title("진사이트")
                .content("신축아파트")
                .user(user)
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("진싸이트")
                .content("신축아파트")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}", post.getId()) //PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 삭제")
    @jinSiteMockUser()
    void test8() throws Exception {
        //given
        User user = userRepository.findAll().get(0);

        Post post = Post.builder()
                .title("진사이트")
                .content("신축아파트")
                .user(user)
                .build();
        postRepository.save(post);

        //expected
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test9() throws Exception {
        //expected
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    @jinSiteMockUser()
    void test10() throws Exception {

        PostEdit postEdit = PostEdit.builder()
                .title("진싸이트")
                .content("신축아파트")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}", 1L) //PATCH /posts/{postId}
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postEdit)))
                .andExpect(status().isNotFound())
                .andDo(print());
        //expected
    }
}
//    @Test
//    @DisplayName("게시글 작성시 제목이 '바보'는 포함될 수 없다.")
//    void tes11() throws Exception {
//        //given
//        PostCreate request = PostCreate.builder()
//                .title("나는 바보입니다.")
//                .content("반포자이")
//                .build();
//
//        String json = objectMapper.writeValueAsString(request);
//
//        //when
//        mockMvc.perform(post("/posts")
//                        .contentType(APPLICATION_JSON)
//                        .content(json)
//                )
//                .andExpect(status().isBadRequest())
//                .andDo(print());
//    }


//API문서 생성

//GET /posts/{postId} -> 단건조회
//POST / posts -> 게시글 등록
//클라이언트 입장 어떤 API 있는지 모름 그래서 API를 잘 문서화 해서 전달을 해 드려야함.
//만든 라우팅 규칙을 잘 파싱을 해서 자동으로 문서화 해주는 툴들이 있다, 라이브러리도 있음
//우리는 Spring REST Dock 사용 =>
// 이유는 운영중인 코드에 영향이 없다.
// 변경된 기능에 대해서 최신 문서 유지가 어느정도 가능하다.
// 코드 수정 -> 문서를 수정 x -> 그럼 코드 문서가 서로 안맞음
// 그런데 레스토 독스 같은 경우에는 테스트 케이스를 만들어서 실해을 하고 그거를 토대로 통과가
// 됐을 때 문서를 자동으로 생성을 해준다.