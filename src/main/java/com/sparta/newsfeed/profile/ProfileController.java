package com.sparta.newsfeed.profile;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.profile.dto.ProfileRequestDto;
import com.sparta.newsfeed.profile.dto.ProfileResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<BaseResponse<ProfileResponseDto>> findOne(@PathVariable Long userId) {
        ProfileResponseDto dto = profileService.findOne(userId);
        return ResponseEntity.ok(BaseResponse.of("프로필 조회", 200, dto));
    }

    @PutMapping
    public ResponseEntity<BaseResponse<ProfileResponseDto>> update(@PathVariable Long userId,
                                                                   @Valid @RequestBody ProfileRequestDto request,
                                                                   @AuthenticationPrincipal UserDetails userDetails) {
        ProfileResponseDto dto = profileService.update(userId, userDetails.getUsername(), request);
        return ResponseEntity.ok(BaseResponse.of("프로필 수정", 200, dto));
    }
}
