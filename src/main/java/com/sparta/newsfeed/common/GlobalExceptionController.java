package com.sparta.newsfeed.common;


import com.sparta.newsfeed.common.exception.comment.NoPrivilegesException;
import com.sparta.newsfeed.common.exception.comment.NotFoundCommentException;
import com.sparta.newsfeed.common.exception.like.AlreadyLikeException;
import com.sparta.newsfeed.common.exception.like.NotFoundLikeException;
import com.sparta.newsfeed.common.exception.like.SelfLikeException;
import com.sparta.newsfeed.common.exception.post.NotFoundPostException;
import com.sparta.newsfeed.common.exception.post.OnlyAuthorAccessException;
import com.sparta.newsfeed.common.exception.profile.NotFoundProfileException;
import com.sparta.newsfeed.common.exception.user.*;
import com.sparta.newsfeed.user.exception.ExpiredAuthCodeException;
import com.sparta.newsfeed.user.exception.WrongAuthCodeException;
import com.sparta.newsfeed.user.follow.exception.NotFoundFollowException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler(NotFoundUserException.class)
    public ResponseEntity<BaseResponse<Void>> notFoundUserException(NotFoundUserException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                false, null));
    }

    @ExceptionHandler(InvalidUserException.class)
    public ResponseEntity<BaseResponse<Void>> invalidUserException(InvalidUserException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                false, null));
    }

    @ExceptionHandler(NotFoundPostException.class)
    public ResponseEntity<BaseResponse<Void>> invalidUserException(NotFoundPostException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                false, null));
    }

    @ExceptionHandler(OnlyAuthorAccessException.class)
    public ResponseEntity<BaseResponse<Void>> invalidUserException(OnlyAuthorAccessException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                false, null));
    }

    @ExceptionHandler(SelfLikeException.class)
    public ResponseEntity<BaseResponse<Void>> invalidUserException(SelfLikeException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                false, null));
    }

    @ExceptionHandler(ExistingUserException.class)
    public ResponseEntity<BaseResponse<Void>> existingUserException(ExistingUserException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                false, null));
    }

    @ExceptionHandler(WrongPasswordException.class)
    public ResponseEntity<BaseResponse<Void>> wrongPasswordException(WrongPasswordException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                false, null));
    }

    @ExceptionHandler(SamePasswordException.class)
    public ResponseEntity<BaseResponse<Void>> samePasswordException(SamePasswordException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                false, null));
    }


    @ExceptionHandler(WrongAuthCodeException.class)
    public ResponseEntity<BaseResponse<Void>> wrongAuthCodeException(WrongAuthCodeException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                false, null));
    }

    @ExceptionHandler(ExpiredAuthCodeException.class)
    public ResponseEntity<BaseResponse<Void>> expiredAuthCodeException(ExpiredAuthCodeException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                false, null));
    }

    @ExceptionHandler(NoPrivilegesException.class)
    public ResponseEntity<BaseResponse<Void>> handleNoPrivilegesException(NoPrivilegesException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(), false, null));
    }

    @ExceptionHandler(NotFoundCommentException.class)
    public ResponseEntity<BaseResponse<Void>> handleNotFoundCommentException(NotFoundCommentException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(), false, null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Void>> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        String errorMessage = exception.getBindingResult().getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(" "));

        return ResponseEntity.badRequest()
                .body(BaseResponse.of(errorMessage, false, null));
    }

    @ExceptionHandler(NotFoundLikeException.class)
    public ResponseEntity<BaseResponse<Void>> notFoundLikeException(NotFoundLikeException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(), false, null));
    }

    @ExceptionHandler(NotFoundFollowException.class)
    public ResponseEntity<BaseResponse<Void>> notFoundFollowException(NotFoundFollowException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(), false, null));
    }

    @ExceptionHandler(NotFoundProfileException.class)
    public ResponseEntity<BaseResponse<Void>> notFoundProfileException(NotFoundProfileException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(), false, null));
    }

    @ExceptionHandler(AlreadyLikeException.class)
    public ResponseEntity<BaseResponse<Void>> alreadyLikeException(AlreadyLikeException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(), false, null));
    }
}
