package com.sparta.newsfeed.profile;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.profile.dto.ProfileRequestDto;
import com.sparta.newsfeed.profile.dto.ProfileResponseDto;
import com.sparta.newsfeed.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/profiles")
@Tag(name = "Profile", description = "프로필 API")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @Operation(summary = "유저 프로필 조회", description = "유저의 프로필 정보를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    public ResponseEntity<BaseResponse<ProfileResponseDto>> findProfile(@PathVariable Long userId) {
        ProfileResponseDto dto = profileService.findProfile(userId);
        return ResponseEntity.ok(BaseResponse.of("프로필 조회", true, dto));
    }


    @PostMapping
    @Operation(summary = "유저 프로필 생성", description = "유저의 프로필 정보를 생성하고 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "생성 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    public ResponseEntity<BaseResponse<ProfileResponseDto>> createProfile(@PathVariable Long userId,
                                                                          @Valid @RequestBody ProfileRequestDto request) {
        ProfileResponseDto dto = profileService.createProfile(userId, request);
        return new ResponseEntity<>(BaseResponse.of("프로필 생성", true, dto), HttpStatus.CREATED);
    }

    @PutMapping
    @Operation(summary = "유저 프로필 수정", description = "유저의 프로필 정보를 수정하고 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    public ResponseEntity<BaseResponse<ProfileResponseDto>> updateProfile(@PathVariable Long userId,
                                                                          @Valid @RequestBody ProfileRequestDto request,
                                                                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileResponseDto dto = profileService.updateProfile(userId, userDetails.getUser(), request);
        return ResponseEntity.ok(BaseResponse.of("프로필 수정", true, dto));
    }

    @PutMapping(value = "/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "유저 프로필 이미지 수정", description = "유저의 프로필 이미지를 수정하고 저장한 S3 Url을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "프로필 이미지 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    public ResponseEntity<BaseResponse<String>> uploadProfileImage(@PathVariable Long userId,
                                                                   @RequestParam(value = "image") MultipartFile image,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        String imageUrl = profileService.uploadProfileImage(userId, userDetails.getUser(), image);
        return ResponseEntity.ok(BaseResponse.of("프로필 사진 업로드", true, imageUrl));
    }

    @GetMapping("/image")
    @Operation(summary = "유저 프로필 이미지 조회", description = "유저의 프로필 이미지를 저장한 S3 Url을 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    public ResponseEntity<BaseResponse<String>> getProfileImage(@PathVariable Long userId,
                                                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        String imageUrl = profileService.getProfileImage(userId, userDetails.getUser());
        return ResponseEntity.ok(BaseResponse.of("프로필 사진 조회", true, imageUrl));
    }

    @DeleteMapping("/image")
    @Operation(summary = "유저 프로필 이미지 삭제", description = "유저의 프로필 이미지를 S3에서 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    public ResponseEntity<BaseResponse<Void>> deleteProfileImage(@PathVariable Long userId,
                                                                 @AuthenticationPrincipal UserDetailsImpl userDetails) {
        profileService.deleteProfileImage(userId, userDetails.getUser());
        return ResponseEntity.ok(BaseResponse.of("프로필 사진 삭제", true, null));
    }
}
