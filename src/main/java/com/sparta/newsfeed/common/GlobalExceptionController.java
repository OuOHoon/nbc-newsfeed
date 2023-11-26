package com.sparta.newsfeed.common;

import com.sparta.newsfeed.common.exception.comment.NoPrivilegesException;
import com.sparta.newsfeed.common.exception.comment.NotFoundCommentException;
import com.sparta.newsfeed.common.exception.comment.NotFoundPostException;
import com.sparta.newsfeed.common.exception.InvalidUserException;
import com.sparta.newsfeed.common.exception.NotFoundPostException;
import com.sparta.newsfeed.common.exception.NotFoundUserException;
import com.sparta.newsfeed.post.OnlyAuthorAccessException;
import com.sparta.newsfeed.like.SelfLikeException;
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

    @ExceptionHandler(NotFoundPostException.class)
    public ResponseEntity<BaseResponse<Void>> invalidUserException(NotFoundPostException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                HttpStatus.BAD_REQUEST.value(), null));
    }

    @ExceptionHandler(OnlyAuthorAccessException.class)
    public ResponseEntity<BaseResponse<Void>> invalidUserException(OnlyAuthorAccessException e) {
        return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(),
                HttpStatus.BAD_REQUEST.value(), null));
    }

    @ExceptionHandler(SelfLikeException.class)
    public ResponseEntity<BaseResponse<Void>> invalidUserException(SelfLikeException e) {
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

    @ExceptionHandler(NoPrivilegesException.class)
    public ResponseEntity<String> handleNoPrivilegesException(NoPrivilegesException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundCommentException.class)
    public ResponseEntity<String> handleNotFoundCommentException(NotFoundCommentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(NotFoundPostException.class)
    public ResponseEntity<String> handleNotFoundPostException(NotFoundPostException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
