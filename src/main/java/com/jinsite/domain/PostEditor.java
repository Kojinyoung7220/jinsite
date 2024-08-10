package com.jinsite.domain;

import lombok.Getter;

/**
 * Post에 change메서드를 만들어 title과 content를 저장할 수도 있지만
 * 어떤 싸이코 개발자가 순서를 바꿀 수 있으니 안전장치를
 * PostEditor클래스를 통해서 만들자. 이제는  Post클래스에서 ->toEditor를 통해서 바꾼다
 */
@Getter
public class PostEditor {

    private final String title;
    private final String content;

    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public static PostEditorBuilder builder() {
        return new PostEditorBuilder();
    }

    public static class PostEditorBuilder {
        private String title;
        private String content;

        PostEditorBuilder() {
        }

        public PostEditorBuilder title(final String title) {
            if(title != null){
                this.title = title;
            }
            return this;
        }

        public PostEditorBuilder content(final String content) {
            if(content != null){
                this.content = content;
            }
            return this;
        }

        public PostEditor build() {
            return new PostEditor(this.title, this.content);
        }

        public String toString() {
            return "PostEditor.PostEditorBuilder(title=" + this.title + ", content=" + this.content + ")";
        }
    }
}

