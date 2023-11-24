package com.sparta.newsfeed.common.exception.like;

public class SelfLikeException extends IllegalArgumentException {
    private static final String MESSAGE = "본인이 작성한 글에 좋아요 할 수 없습니다.";
    public SelfLikeException() {
        super(MESSAGE);
    }
}
