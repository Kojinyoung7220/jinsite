package com.jinsite.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 *  {
 *      "code" : "400",
 *      "message": "잘못된 요청입니다.",
 *      "validation": {
 *          "title": "값을 입력해주세요"
 *      }
 *
 *  }
 */
@Getter
//@JsonInclude(value = JsonInclude.Include.NON_EMPTY) //널이나 비어있는 값 빼서 json으로 보내기.
public class ErrorResponse {

    private final String code;  //에러코드 때문에 int로 고정해도 되지만 향후를 위해 String으로 일단 고정.
    private final String message;
    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String,String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
