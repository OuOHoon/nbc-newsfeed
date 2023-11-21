package com.sparta.newsfeed.user;

public class InvalidUserException extends RuntimeException {
    private static final String MESSAGE = "유효한 유저가 아닙니다.";

    public InvalidUserException() {
        super(MESSAGE);
    }
}
