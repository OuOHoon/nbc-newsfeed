package com.sparta.newsfeed.common.exception.like;

public class NotFoundLikeException extends RuntimeException {
    private static final String MESSAGE = "좋아요 기록이 없습니다.";

    public NotFoundLikeException() {
        super(MESSAGE);
    }
}
