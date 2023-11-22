package com.sparta.newsfeed.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
public class LoginRequestDto {

    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    private String password;

}
