package com.sparta.newsfeed.common.exception.follow;

public class SelfFollowException extends RuntimeException {
    private final static String MESSAGE = "본인이 본인을 팔로우 할 수 없습니다.";

    public SelfFollowException() {
        super(MESSAGE);
    }
}
