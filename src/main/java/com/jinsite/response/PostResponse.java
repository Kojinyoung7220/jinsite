package com.jinsite.response;

import com.jinsite.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
/**
 * 서비스 정책에 맞는 클래스 -> 만약 요구사항으로 타이틀의 글자를 10글자로 제한 시켜 달라.
 *                          그러면 getter메서드 안에 로직을 작성해 제한하면 모든 로직들이 10글자로 제한 되기 때문에
 *                          각각의 요구사항에 맞게 규칙을 정하기 위해 PostResponse 클래스로 분리 하였음,!!
 */
@Getter
@Builder
public class PostResponse {

    private final Long id;

    private final String title;

    private final String content;

    //생성자 오버로딩
    public PostResponse(Post post){
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
    }

    public PostResponse(Long id, String title, String content) {
        this.id = id;
        this.title = title.substring(0, Math.min(title.length(), 10));
        this.content = content;
    }
}
