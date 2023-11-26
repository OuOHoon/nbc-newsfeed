package com.sparta.newsfeed.user.exception;

import java.util.concurrent.TimeoutException;

public class ExpiredAuthCodeException extends IllegalArgumentException {
    private static final String MESSAGE = "인증번호 만료 기한이 지났습니다. 다시 메일 인증을 시도해주세요.";

    public ExpiredAuthCodeException() {
        super(MESSAGE);
    }
}
