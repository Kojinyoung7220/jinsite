package com.jinsite.request;

import com.jinsite.exception.InvalidRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 컨트롤러 부분에서 "/posts"로 요청이 올 경우
 * Map<String, String> params 의 형태의 클래스를 따로 만듬. =>DTO라고 생각하자.
 *
 */
@Setter
@Getter
@ToString
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;

    @NotBlank(message = "컨텐츠를 입력해주세요.")
    private String content;

    //빌더를 클래스 위에 다는 방법이랑 생성자에 다는 방법이 있는데 생성자에 다는 방법을 추천
    // 그 이유는 클래스에 달게 되면 final이 붙어 버리면 작동이 안될 수도 있음.
    // 또한 @NoArgsConstructor이 붙게되면 혼돈이 온다.. 검색해서 알아보자.
    // 빌더 클래스 같은 경우에는 명확하게 그 어떤 값에 대한 이 필드들에 대한 생성자가 있어야 한다.
    //생성자에 한번 다는 걸 해보고 그 다음에 바꿔보자. https://mangkyu.tistory.com/163
    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }

//    public void validate(){
//        if(title.contains("바보")){
//            throw new InvalidRequest("title", "제목에 바보를 포함할 수 없습니다.");
//        }
//    }


    //빌더의 장점 -> 면접에 나올 수 있다
    // -가독성이 좋다 (값 생성에 대한 유연함)
    // -필요햔 값만 받을 수 있다. // ->(오버로딩 가능한 조건 찾아보세요)
    // -객체의 불변성
}

