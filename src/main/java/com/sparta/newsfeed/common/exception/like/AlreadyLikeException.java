package com.sparta.newsfeed.common.exception.like;

public class AlreadyLikeException extends RuntimeException {
    private static final String MESSAGE = "이미 좋아요를 했습니다.";

    public AlreadyLikeException() {
        super(MESSAGE);
    }
}
