package com.sparta.newsfeed.common.exception.user;

public class ExistingUserException extends IllegalArgumentException{

    private static final String MESSAGE = "이미 존재하는 유저 아이디입니다.";

    public ExistingUserException() {
        super(MESSAGE);
    }
}
