package com.jinsite.exception;

import lombok.Getter;

/**
 * 이 예외는 언제 생기냐
 * post 컨트롤러에 보면 @Valid라는 어노테이션이 붙여져 있는 것을 볼 수 있다.
 * postcreate DTO안에 @NOTBLANK 어노테이션을 통해서 쉽게 검증을 했었는데
 * 뭔가 이런 어노테이션 같은 걸로 해결할 수 없는 복잡한 그런 검증이 추가적으로 필요할 때
 * ex) post컨트롤러에서 바보라는 단어가 들어가면 if문으로 InvalidRequest 터트리면서
 * 바보라는 단어가 못들어가게 막을 수 있다. => ++ 컨트롤러에서 if문은 안좋은 방식.
 * -> 데이터를 꺼내와서 조합하고 검증하는 건 가능하면 하지 말자.
 * =====> 가능하면 메시지를 던지자!!!!
 *
 * InvalidRequest는 정책상
 * status -> 400        으로 내려줘야함.
 */
@Getter
public class InvalidRequest extends JinSiteException{

    private static final String MESSAGE = "잘못된 요청입니다!!.";

    public InvalidRequest() {
        super(MESSAGE);
    }
    public InvalidRequest(String fieldName, String message) {
        super(MESSAGE);
        addValidation(fieldName, message);
    }
    @Override
    public int getStatusCode(){
        return 400;
    }
}