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
    private Long user_id;
    private String text;

    private Long comment_id;
    private ZonedDateTime date;


    public CommentResponseDto(Comment comment) {
        this.comment_id = comment.getId();
        this.post = comment.getPost().getId();
        this.text = comment.getText();
        this.date = comment.getDate();
        this.user_id = comment.getUser().getId();
    }
}
