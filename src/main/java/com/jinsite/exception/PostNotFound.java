package com.jinsite.exception;

/**
 * status -> 404 로 내려줘야지 이상적.
 */
public class PostNotFound extends JinSiteException{

    private static final String MESSAGE = "존재하지 않는 글입니다.";

    public PostNotFound() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
