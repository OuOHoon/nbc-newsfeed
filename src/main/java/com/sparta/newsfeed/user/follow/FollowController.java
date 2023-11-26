package com.sparta.newsfeed.user.follow;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.user.follow.dto.FollowResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/follows")
@Tag(name = "Follow", description = "팔로우 API")
public class FollowController {

    private final FollowService followService;

    @PostMapping
    @Operation(summary = "팔로우", description = "유저를 팔로우합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로우 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    public ResponseEntity<BaseResponse<Void>> follow(@PathVariable Long userId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.follow(userId, userDetails.getUser());
        return new ResponseEntity<>(BaseResponse.of("팔로우 추가", true, null),
                HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "팔로우 조회", description = "유저의 팔로우 정보를 반환합니다.")
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
    public ResponseEntity<BaseResponse<FollowResponseDto>> getFollows(@PathVariable Long userId) {
        FollowResponseDto follows = followService.getFollows(userId);
        return ResponseEntity.ok(BaseResponse.of("팔로우 조회", true, follows));
    }

    @DeleteMapping
    @Operation(summary = "팔로우 취소", description = "유저 팔로우를 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "팔로우 취소 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    public ResponseEntity<BaseResponse<Void>> unfollow(@PathVariable Long userId,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.unfollow(userId, userDetails.getUser());
        return ResponseEntity.ok(BaseResponse.of("팔로우 삭제", true, null));
    }
}
