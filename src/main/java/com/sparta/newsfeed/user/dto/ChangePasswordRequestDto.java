package com.sparta.newsfeed.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDto {

    @NotBlank
    @NotNull
    private String existingpassword;

    @NotBlank
    @NotNull
    private String newpassword;

}
