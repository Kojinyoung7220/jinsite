package com.jinsite.exception;

public class AlreadyExistsEmailException extends JinSiteException{

    private static final String Message = "이미 가입된 이메일입니다.";

    public AlreadyExistsEmailException() {
        super(Message);
    }

    public int getStatusCode(){
        return 400;
    }
}
