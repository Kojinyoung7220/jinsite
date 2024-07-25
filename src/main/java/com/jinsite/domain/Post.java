package com.jinsite.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

/**
 *         postCreate는 현재 requestDTO 형태이지 엔티티가 아니기 때문에 못들어간다.
 *         그래서 일반 엔티티 형식으로 변환 해야한다. postCreate -> Entity
 *         Post post = new Post(postCreate.getTitle(), postCreate.getContent());
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob //자바에선 String db에서는 Long 텍스트 형태로 가지고 있겠다.
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public PostEditor.PostEditorBuilder toEditor(){
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        this.title = postEditor.getTitle();
        this.content = postEditor.getContent();
    }
}