package com.sparta.newsfeed.post.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostRequestDto {
    @Size(max = 30, message = "제목은 최대 30자까지 작성 가능합니다.")
    private String title;
    private String contents;
}
