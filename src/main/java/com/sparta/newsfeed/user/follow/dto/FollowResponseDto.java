package com.sparta.newsfeed.user.follow.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class FollowResponseDto {

    private List<String> followers;
    private List<String> followings;
}
