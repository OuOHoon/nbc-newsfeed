package com.sparta.newsfeed.comment.exception;

public class NotFoundCommentException extends RuntimeException {
	public NotFoundCommentException(String message) {
		super(message);
	}
}