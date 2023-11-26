package com.sparta.newsfeed.common.exception.post;

public class NotFoundPostException extends RuntimeException {
    private static final String MESSAGE = "포스트를 찾을 수 없습니다.";

    public NotFoundPostException() {
        super(MESSAGE);
    }
}

