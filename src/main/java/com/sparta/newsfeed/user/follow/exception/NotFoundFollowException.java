package com.sparta.newsfeed.user.follow.exception;

public class NotFoundFollowException extends RuntimeException {
    private static final String MESSAGE = "팔로우를 찾지 못했습니다.";

    public NotFoundFollowException() {
        super(MESSAGE);
    }
}
