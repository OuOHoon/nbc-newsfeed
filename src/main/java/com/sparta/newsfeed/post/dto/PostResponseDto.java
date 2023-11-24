package com.sparta.newsfeed.post.dto;


import com.sparta.newsfeed.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Long id;
    private String title;
    private String contents;
//    private String username;
    private int likesCount;
    private LocalDateTime createdAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.contents = post.getContents();
 //       this.username = post.getUser().getProfile().getNickname();
        this.likesCount = post.countLikes();
        this.createdAt = post.getCreatedAt();
    }
}
