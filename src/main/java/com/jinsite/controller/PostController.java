package com.jinsite.controller;

import com.jinsite.request.PostCreate;
import com.jinsite.request.PostEdit;
import com.jinsite.request.PostSearch;
import com.jinsite.response.PostResponse;
import com.jinsite.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Request,Response 분리
 * Request 클래스
 * => 계시글(post)를 작성할 때 썼던 postCreate 요청과 벨리데이션 할 수 있는 정책을 담아둔 클래스이다.
 * Response 클래스
 * => 서비스 정책에 맞는 로직이 들어갈 수 있는 클래스를 만들었다.
 *
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        //Case1. 저장한 데이터 Entity -> response로 응답하기
        //Case2. 저장한 데이터의 primary_id -> response로 응답하기
        //            Client에서는 수신한 id를 글 조회 API를 통해서 데이터를 수신받음
        //Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함.
        //Bad Case: 서버에서 -> 반드시 이렇게 할겁니다! fix하는 방식은 좋지않다.
        //          ->서버에서 차라리 유연하게 대응하는게 좋다.! ->코드를 잘짜야함..!!
        //          -> 한 번에 일괄적으로 잘 처리되는 케이스가 없다
        //          -> 잘 관리하는 형태가 중요하다!
        request.validate();
        //인증을 어떻게 받아야 할까?
        //1. GET Parameter로 받는다
        //2. POST(body) value 로 받는다
        //3. Header로 받는다.
        // => 하지만 다 필요없고 스프링 인터셉터로 해결 가능!~~
        postService.write(request);
    }

    /**
     * /posts -> 글 전체 조회(검색 + 페이징)
     * /posts/{postId} -> 글 한개만 조회
     */
    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        //만약 tilte값을 10글자로 주세요~ 라고 왔을때 get안에 로직을 넣지말고,
        //응답 클래스를 분리하자 (서비스 정책에 맞는)
        return postService.get(postId);

    }

    /*
    조회 API
    지난시간 = 단건 조회 API(1개의 글 Post를 가져오는 기능)
    이번시간 = 여러개의 글을 조회 API
    => 게시글 목록 같은 경우
    /posts get맵핑을 이용해 만들것이다~~
     */
    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public void edit(@PathVariable Long postId, @RequestBody @Valid PostEdit request) {
        postService.edit(postId, request);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId){
        postService.delete(postId);
    }

}
