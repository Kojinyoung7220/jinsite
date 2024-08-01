package com.jinsite.exception;

/**
 * db에서 id(email), password를 입력시 실패했을때의 오류
 */
public class InvalidSingInInformation extends JinSiteException {

    private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다.";

    public InvalidSingInInformation() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
