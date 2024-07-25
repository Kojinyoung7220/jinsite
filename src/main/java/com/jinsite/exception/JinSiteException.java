package com.jinsite.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

//1. 나머지 예외들 RuntimeException-> JinSiteException 상속 바꾸기.
@Getter
public abstract class JinSiteException extends RuntimeException {
//abstract 만든 이유는?

    public final Map<String, String> validation = new HashMap<>();

    public JinSiteException(String message) {
        super(message);
    }

    public JinSiteException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getStatusCode();

    public void addValidation(String fieldName, String message){
        validation.put(fieldName, message);
    }
}
