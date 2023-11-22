package com.sparta.newsfeed.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {

    @Pattern(regexp = "^[a-z0-9]{6,12}$")
    //최소 6자 이상, 12자 이하이며 알파벳 소문자(a~z), 숫자(0~9)
    private String username;

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9]{8,15}+$")
    //최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9)
    private String password;

}
