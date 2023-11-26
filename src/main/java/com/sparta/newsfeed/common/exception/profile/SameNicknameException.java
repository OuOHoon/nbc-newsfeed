package com.sparta.newsfeed.common.exception.profile;

public class SameNicknameException extends RuntimeException {
    private final static String MESSAGE = "이미 존재하는 닉네임 입니다.";

    public SameNicknameException() {
        super(MESSAGE);
    }
}
