package com.sparta.newsfeed.common.exception.post;

public class OnlyAuthorAccessException extends IllegalArgumentException {

    private static final String MESSAGE = "작성자만 수정/삭제 가능합니다.";
    public OnlyAuthorAccessException() {
        super(MESSAGE);
    }
}