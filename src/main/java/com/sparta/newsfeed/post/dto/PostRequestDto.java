package com.sparta.newsfeed.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostRequestDto {
    
    @Size(max = 30, message = "제목은 최대 30자까지 작성 가능합니다.")
    @Schema(description = "제목")
    private String title;
    
    @Schema(description = "내용")
    private String contents;
}
