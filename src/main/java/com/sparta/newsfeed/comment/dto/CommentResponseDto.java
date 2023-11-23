package com.sparta.newsfeed.comment.dto;

import com.sparta.newsfeed.comment.Comment;
import com.sparta.newsfeed.comment.CommentRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@AllArgsConstructor
@Builder
@Getter
public class CommentResponseDto {

    private Long feed_id;
    private Long user_id;
    private String text;

    private Long comment_id;
    private ZonedDateTime date;


    public CommentResponseDto(Comment comment) {
        this.comment_id = comment.getComment_id();
        this.feed_id = comment.getFeed().getFeed_id();
        this.text = comment.getText();
        this.date = comment.getDate();
        this.user_id = comment.getUser().getId();
    }
}
