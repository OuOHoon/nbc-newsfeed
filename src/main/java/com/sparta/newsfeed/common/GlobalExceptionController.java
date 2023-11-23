package com.sparta.newsfeed.common;

import com.sparta.newsfeed.common.exception.InvalidUserException;
import com.sparta.newsfeed.common.exception.NotFoundUserException;
import com.sparta.newsfeed.user.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<BaseResponse<Void>> notFoundUserException(NotFoundUserException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                HttpStatus.BAD_REQUEST.value(), null));
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<BaseResponse<Void>> invalidUserException(InvalidUserException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                HttpStatus.BAD_REQUEST.value(), null));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Void>> invalidUserException(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                HttpStatus.BAD_REQUEST.value(), null));
    }

    @ExceptionHandler(ExistingUserException.class)
    public ResponseEntity<BaseResponse<Void>> existingUserException(ExistingUserException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                HttpStatus.BAD_REQUEST.value(), null));
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<BaseResponse<Void>> wrongPasswordException(WrongPasswordException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                HttpStatus.BAD_REQUEST.value(), null));
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<BaseResponse<Void>> samePasswordException(SamePasswordException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                HttpStatus.BAD_REQUEST.value(), null));
    }
}
