package com.sparta.newsfeed.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailAuthRequestDto {

    @NotBlank
    @NotNull
    private int authcode;

}
