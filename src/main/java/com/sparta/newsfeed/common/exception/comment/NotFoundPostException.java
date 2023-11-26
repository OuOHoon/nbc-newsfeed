package com.sparta.newsfeed.common.exception.comment;

public class NotFoundPostException  extends RuntimeException {
	private static final String MESSAGE = "게시글이 존재하지 않습니다.";

	public NotFoundPostException() {
		super(MESSAGE);
	}

}