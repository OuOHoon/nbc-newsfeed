package com.sparta.newsfeed.feed;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedRequestDto {
    @Size(max = 30, message = "제목은 최대 30자까지 작성 가능합니다.")
    private String title;
    private String contents;
}
