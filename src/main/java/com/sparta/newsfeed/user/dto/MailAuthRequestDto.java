package com.sparta.newsfeed.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MailAuthRequestDto {

    @Pattern(regexp = "/[0-9]{6}$/", message = "인증번호는 6자리 숫자입니다.")
    private int authcode;

}
