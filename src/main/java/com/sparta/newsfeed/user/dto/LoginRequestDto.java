package com.sparta.newsfeed.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

    @NotBlank
    @NotNull
    private String username;

    @NotBlank
    @NotNull
    private String password;

}
