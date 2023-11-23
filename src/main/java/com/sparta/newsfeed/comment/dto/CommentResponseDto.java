package com.sparta.newsfeed.comment.dto;

import com.sparta.newsfeed.comment.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Builder
@Getter
public class CommentResponseDto {

    private Long post;
    private Long user;
    private String text;

    private Long comment_id;
    private ZonedDateTime date;


    public CommentResponseDto(Comment comment) {
        this.comment_id = comment.getComment_id();
        this.post = comment.getPost().getId();
        this.text = comment.getText();
        this.date = comment.getDate();
        this.user = comment.getUser().getId();
    }
}
