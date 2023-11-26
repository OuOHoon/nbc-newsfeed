package com.sparta.newsfeed.profile;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.profile.dto.ProfileRequestDto;
import com.sparta.newsfeed.profile.dto.ProfileResponseDto;
import com.sparta.newsfeed.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<BaseResponse<ProfileResponseDto>> findProfile(@PathVariable Long userId) {
        ProfileResponseDto dto = profileService.findProfile(userId);
        return ResponseEntity.ok(BaseResponse.of("프로필 조회", true, dto));
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ProfileResponseDto>> createProfile(@PathVariable Long userId,
                                                                          @Valid @RequestBody ProfileRequestDto request) {
        ProfileResponseDto dto = profileService.createProfile(userId, request);
        return new ResponseEntity<>(BaseResponse.of("프로필 생성", true, dto), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<BaseResponse<ProfileResponseDto>> updateProfile(@PathVariable Long userId,
                                                                          @Valid @RequestBody ProfileRequestDto request,
                                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileResponseDto dto = profileService.updateProfile(userId, userDetails.getUser(), request);
        return ResponseEntity.ok(BaseResponse.of("프로필 수정", true, dto));
    }

    @RequestMapping(value = "/image", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<BaseResponse<String>> uploadProfileImage(@PathVariable Long userId,
                                                                   @RequestParam(value = "image") MultipartFile image,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String imageUrl = profileService.uploadProfileImage(userId, userDetails.getUser(), image);
        return ResponseEntity.ok(BaseResponse.of("프로필 사진 업로드", true, imageUrl));
    }

    @GetMapping("/image")
    public ResponseEntity<BaseResponse<String>> getProfileImage(@PathVariable Long userId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String imageUrl = profileService.getProfileImage(userId, userDetails.getUser());
        return ResponseEntity.ok(BaseResponse.of("프로필 사진 조회", true, imageUrl));
    }

    @DeleteMapping("/image")
    public ResponseEntity<BaseResponse<Void>> deleteProfileImage(@PathVariable Long userId,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        profileService.deleteProfileImage(userId, userDetails.getUser());
        return ResponseEntity.ok(BaseResponse.of("프로필 사진 삭제", true, null));
    }
}
