package com.sparta.newsfeed.user.exception;

public class WrongAuthCodeException extends IllegalArgumentException {

    private static final String MESSAGE = "인증번호가 올바르지 않습니다.";

    public WrongAuthCodeException() { super(MESSAGE); }
}
