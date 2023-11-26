package com.sparta.newsfeed.common.exception.profile;

public class NotFoundProfileException extends RuntimeException {
    private final static String MESSAGE = "프로필을 찾을 수 없습니다.";

    public NotFoundProfileException() {
        super(MESSAGE);
    }
}
