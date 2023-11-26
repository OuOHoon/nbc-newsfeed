package com.sparta.newsfeed.post;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private String title;
    private String contents;
    private String username;
    private int likesCount;

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.username = post.getUser().getProfile().getNickname();
        this.likesCount = post.getLikesCount();
    }
}
