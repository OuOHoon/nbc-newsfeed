package com.sparta.newsfeed.common.exception.comment;

public class NoPrivilegesException extends RuntimeException {

	private static final String MESSAGE = "댓글에 대한 권한이 없습니다.";

	public NoPrivilegesException() {
		super(MESSAGE);
	}
}