package com.sparta.newsfeed.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDto {

    @NotBlank
    @Schema(name = "existing password", example = "sk1fmfbkv", required = true)
    @Pattern(regexp = "^[A-Za-z0-9]{8,15}$", message = "올바르지 않은 비밀번호 형식입니다.")
    private String existingpassword;

    @NotBlank
    @Schema(name = "new password", example = "sk2fmfbkv", required = true)
    @Pattern(regexp = "^[A-Za-z0-9]{8,15}$", message = "올바르지 않은 비밀번호 형식입니다.")
    private String newpassword;

}
