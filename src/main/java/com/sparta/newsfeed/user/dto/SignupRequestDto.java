package com.sparta.newsfeed.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @Schema(name = "username", example = "abcdegf@gmail.com", required = true)
    @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-.]+$", message = "올바르지 않은 이메일 형식입니다.")
    //이메일 형식만 가능
    private String username;

    @NotBlank
    @Schema(name = "password", example = "sk1fmfbkv", required = true)
    @Pattern(regexp = "^[A-Za-z0-9]{8,15}$", message = "올바르지 않은 비밀번호 형식입니다.")
    //최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)
    private String password;

}
