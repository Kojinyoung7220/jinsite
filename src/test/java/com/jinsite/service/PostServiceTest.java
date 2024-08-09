package com.jinsite.service;

import com.jinsite.domain.Post;
import com.jinsite.domain.User;
import com.jinsite.exception.PostNotFound;
import com.jinsite.repository.post.PostRepository;
import com.jinsite.repository.UserRepository;
import com.jinsite.request.PostCreate;
import com.jinsite.request.PostEdit;
import com.jinsite.request.PostSearch;
import com.jinsite.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PostServiceTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1(){
        //given

        User user = User.builder()
                .name("jin")
                .email("jin@gmail.com")
                .password("1234")
                .build();
        userRepository.save(user);

        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();
        //when
        postService.write(user.getId(), postCreate);

        //than
        assertEquals(1L, postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.",post.getTitle());
        assertEquals("내용입니다.",post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2(){
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(requestPost);

        //when
        PostResponse response = postService.get(requestPost.getId());

        //than
        assertNotNull(response);
        assertEquals(1L, postRepository.count());
        assertEquals("foo",response.getTitle());
        assertEquals("bar",response.getContent());
    }


    @Test
    @DisplayName("글 1페이지 조회")
    void test3(){
        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                        .mapToObj(i -> {
                            return Post.builder()
                                    .title("진사이트 제목 " + i)
                                    .content("신축아파트 " + i)
                                    .build();
                        }).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);


        PostSearch postSearch =PostSearch.builder()
                .page(1)
                .build();


        //when
        List<PostResponse> posts = postService.getList(postSearch);

        //than
        assertEquals(10L, posts.size());
        assertEquals("진사이트 제목 19", posts.get(0).getTitle());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test4(){
        //given
        Post post =Post.builder()
                .title("진사이트")
                .content("신축아파트")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("진싸이트")
                .content("신축아파트")
                .build();

        //when
        postService.edit(post.getId(), postEdit);

        //than
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다.id = " + post.getId()));

        assertEquals("진싸이트", changePost.getTitle());
    }
    @Test
    @DisplayName("글 내용 수정")
    void test5(){
        //given
        Post post =Post.builder()
                .title("진사이트")
                .content("신축아파트")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title("진싸이트")
                .content("초가집")
                .build();

        //when
        postService.edit(post.getId(), postEdit);

        //than
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다.id = " + post.getId()));

        assertEquals("진싸이트", changePost.getTitle());
        assertEquals("초가집", changePost.getContent());
    }

    @Test
    @DisplayName("글 내용 수정")
    void test6(){
        //given
        Post post =Post.builder()
                .title("진사이트")
                .content("신축아파트")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("초가집")
                .build();

        //when
        postService.edit(post.getId(), postEdit);

        //than
        Post changePost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다.id = " + post.getId()));

        assertEquals("진사이트", changePost.getTitle());
        assertEquals("초가집", changePost.getContent());
    }

    @Test
    @DisplayName("게시글 삭제")
    void test7(){
        //given
        Post post =Post.builder()
                .title("진사이트")
                .content("신축아파트")
                .build();

        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //than
        assertEquals(0, postRepository.count());


    }

    @Test
    @DisplayName("글 1개 조회 - 존재하지 않는 글")
    void test8(){
        //given
        Post post = Post.builder()
                .title("호돌맨")
                .content("반포자이")
                .build();
        postRepository.save(post);

        //expected

        assertThrows(PostNotFound.class, () -> {
            postService.get(post.getId() + 1L);
        });
    }

    @Test
    @DisplayName("게시글 삭제 - 존재하지 않는 글")
    void test9(){
        //given
        Post post =Post.builder()
                .title("진사이트")
                .content("신축아파트")
                .build();

        postRepository.save(post);


        //expected
        assertThrows(PostNotFound.class, () -> {
            postService.delete(post.getId() + 1L);
        });
    }
    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    void test10() {
        //given
        Post post = Post.builder()
                .title("진사이트")
                .content("신축아파트")
                .build();

        postRepository.save(post);

        PostEdit postEdit = PostEdit.builder()
                .title(null)
                .content("초가집")
                .build();

        //expected
        assertThrows(PostNotFound.class, () -> {
            postService.edit(post.getId() + 1L , postEdit);
        });
    }
}