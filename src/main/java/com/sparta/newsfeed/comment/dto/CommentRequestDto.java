package com.sparta.newsfeed.comment.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    @Size(min = 1, max = 400, message = "400자 이하로 입력해주세요.")
    private String text;

}