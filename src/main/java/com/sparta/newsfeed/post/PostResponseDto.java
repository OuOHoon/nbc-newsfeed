package com.sparta.newsfeed.post;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private String title;
    private String contents;

    public PostResponseDto(Post post) {
        this.title = post.getTitle();
        this.contents = post.getContents();
    }
}
