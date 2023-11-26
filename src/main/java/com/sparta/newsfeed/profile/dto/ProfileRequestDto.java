package com.sparta.newsfeed.profile.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequestDto {

    @Size(min = 4, max = 16, message = "닉네임을 4~16자 사이로 입력해주세요.")
    private String nickname;
    private String introduction;
}
