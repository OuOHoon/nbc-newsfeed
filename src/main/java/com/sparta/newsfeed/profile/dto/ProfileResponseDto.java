package com.sparta.newsfeed.profile.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class ProfileResponseDto {

    private String nickname;
    private String introduction;
}
