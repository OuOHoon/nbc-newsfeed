package com.sparta.newsfeed.common.exception.user;

public class NotFoundUserException extends RuntimeException {
    private static final String MESSAGE = "유저를 찾을 수 없습니다.";

    public NotFoundUserException() {
        super(MESSAGE);
    }
}
