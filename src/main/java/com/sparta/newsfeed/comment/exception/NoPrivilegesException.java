package com.sparta.newsfeed.comment.exception;

public class NoPrivilegesException extends RuntimeException {
	public NoPrivilegesException(String message) {
		super(message);
	}
}