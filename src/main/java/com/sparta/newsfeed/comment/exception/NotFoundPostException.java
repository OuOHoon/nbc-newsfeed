package com.sparta.newsfeed.comment.exception;

public class NotFoundPostException  extends RuntimeException {
	public NotFoundPostException (String message) {
		super(message);
	}
}