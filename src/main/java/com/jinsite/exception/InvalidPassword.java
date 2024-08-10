package com.jinsite.exception;

/**
 * 댓글달때 비밀번호가 기존 비번이랑 다를 경우
 */
public class InvalidPassword extends JinSiteException {

    private static final String MESSAGE = "비밀번호가 올바르지 않습니다.";

    public InvalidPassword() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
