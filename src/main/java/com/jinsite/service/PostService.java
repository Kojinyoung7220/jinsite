package com.jinsite.service;

import com.jinsite.domain.Post;
import com.jinsite.domain.PostEditor;
import com.jinsite.exception.PostNotFound;
import com.jinsite.exception.UserNotFound;
import com.jinsite.repository.post.PostRepository;
import com.jinsite.repository.UserRepository;
import com.jinsite.request.post.PostCreate;
import com.jinsite.request.post.PostEdit;
import com.jinsite.request.post.PostSearch;
import com.jinsite.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public void write(Long userId, PostCreate postCreate){
        //postCreate는 현재 requestDTO 형태이지 엔티티가 아니기 때문에
        //못들어간다. 그래서 일반 엔티티 형식으로 변환 해야한다. postCreate -> Entity
//        Post post = new Post(postCreate.getTitle(), postCreate.getContent());

        var user = userRepository.findById(userId)
                .orElseThrow(UserNotFound::new);

        Post post = Post.builder()
                .user(user)
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long id) {
        //이부분 강의 다시보자 새로움.
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        /**
         *  PostController ->   WebPostService  ->  Repository
         *                 ->   PostService
         *
         * 두가지로 나눈다. 하나는 WebPostService와 그냥 Service
         * 그래서 PostResponse를 위해서 뭔가 행위를 하는 그런 서비스 호출은 WebPostService 여기서 담당.
         * ->(요구사항이 있어 분리해야 할때의 클래스??)
         * 그렇지 않고 외부와 연동을 하는 다른 서비스와 통산을 하기 위해 만드는 서비스는 다 PostService 여기에 둔다.
         * 그렇지만 다 Post에 만드는 경우도 많이 있따.
         *
         */

        /**
         * 글이 너무 많은 경우 -> 비용이 너무 많이 든다.
         * 글이 ->100,000,000 -> DB글 모두 조회하는경우 -> DB가 뻗을 수 있다.
         * DB-> 애플리케이션 서버로 전달하는 시간, 트래픽비용 등이 발생할 수 있다.
         */

    }
    //pageable을 사용해 써도 되지만 지금은 페이징 처리하는 것 밖에 없지만 나중에는
    //정렬이라던지 검색 옵션 같은 게 더 추가가 될 수 있다. 그래서 나만의 리퀘스트 클래스를 따로 만들자.
    public List<PostResponse> getList(PostSearch postSearch) {
        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }
    @Transactional
    public void edit(Long id, PostEdit postEdit){
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);


        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();  /// TODO: 2024-08-11   //이 부분 이해가 잘안간다 확인하자
        
        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        post.edit(postEditor);

    }

    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        //존재하는경우
        postRepository.delete(post);

    }
}
