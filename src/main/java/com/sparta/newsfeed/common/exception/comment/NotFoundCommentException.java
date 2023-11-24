package com.sparta.newsfeed.common.exception.comment;

public class NotFoundCommentException extends RuntimeException {
	private static final String MESSAGE = "해당 댓글이 존재하지 않습니다.";

	public NotFoundCommentException() {	super(MESSAGE);	}
}