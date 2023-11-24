package com.sparta.newsfeed.user.exception;

public class SamePasswordException extends IllegalArgumentException{
    private static final String MESSAGE = "기존 비밀번호와 변경하려는 비밀번호가 같습니다.";

    public SamePasswordException() {
        super(MESSAGE);
    }
}
