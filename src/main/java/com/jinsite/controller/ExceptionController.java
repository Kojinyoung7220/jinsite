package com.jinsite.controller;

import com.jinsite.exception.InvalidRequest;
import com.jinsite.exception.JinSiteException;
import com.jinsite.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 스프링에서 제공해주는 MethodArgumentNotValidException 이런거 말고도 체크해야될 익셉션이 많다.
 * 그런 것들은 따로 만들어 둔다. 그 이유는 처리해야 될 내용들이 입섹션 종류마다 조금씩 다르기 때문에
 * 스프링이 제공해주는 입셉션 -> 따로 만든다.
 * 비즈니스에 맞는 입셉션은 공통적으로 처리해보자(하나의 메서드로)-> 딱 규격이 정해져 있는 편
 */
@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody           //여기서 ResponseBody 어노테이션 때문에 response의 값이 json으로 변환되서 클라이언트에 전송된다.!!
    public ErrorResponse invalidRequestHandler(MethodArgumentNotValidException e){
        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다.")
                .build();

        for(FieldError fieldError : e.getFieldErrors()){
            log.info("Start");
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return response;
    }

    /**
     * int statusCode = e.getStatusCode(); 바꾸고
     * ResponseStatus를 빼니깐 기본값인 200을 반환하였음
     * 그래서 스프링에서 기본적으로 제공하는
     * ErrorResponse -> ResponseEntity<ErrorResponse> 로 변환
     * 리턴을 하게 되면 ResponseEntity 객체 자체가 안에서 응답데이터와 스테이터스 같은 것을
     * 종합적으로 관리를 해서 응답을 해준다.
     */

    @ResponseBody
    @ExceptionHandler(JinSiteException.class)
    public ResponseEntity<ErrorResponse> JinSiteException(JinSiteException e){
        int statusCode = e.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                .validation(e.getValidation())
                .build();

        //응답 json validation ->제목에 바보를 포함할 수 없습니다.


        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(body);

        return response;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e){
        log.error("예외발생", e);

        ErrorResponse body = ErrorResponse.builder()
                .code("500")
                .message(e.getMessage())
                .build();

        //응답 json validation ->제목에 바보를 포함할 수 없습니다.


        ResponseEntity<ErrorResponse> response = ResponseEntity.status(500)
                .body(body);

        return response;
    }
}
















