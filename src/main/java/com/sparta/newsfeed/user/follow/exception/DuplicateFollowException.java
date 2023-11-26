package com.sparta.newsfeed.user.follow.exception;

public class DuplicateFollowException extends RuntimeException {
    private static final String MESSAGE = "이미 팔로우 한 상태입니다.";

    public DuplicateFollowException() {
        super(MESSAGE);
    }
}
