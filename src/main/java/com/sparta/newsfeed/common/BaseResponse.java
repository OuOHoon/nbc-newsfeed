package com.sparta.newsfeed.common;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseResponse<T> {

    private final String message;
    private final Integer statusCode;
    private final T body;

    public static <T> BaseResponse<T> of(String message, Integer statusCode, T body) {
        return new BaseResponse<>(message, statusCode, body);
    }
}
