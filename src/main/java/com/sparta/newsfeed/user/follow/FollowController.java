package com.sparta.newsfeed.user.follow;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.user.follow.dto.FollowResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/{userId}/follows")
public class FollowController {

    private final FollowService followService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> follow(@PathVariable Long userId,
                                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.follow(userId, userDetails.getUser());
        return new ResponseEntity<>(BaseResponse.of("팔로우 추가", true, null),
                HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<FollowResponseDto>> getFollows(@PathVariable Long userId) {
        FollowResponseDto follows = followService.getFollows(userId);
        return ResponseEntity.ok(BaseResponse.of("팔로우 조회", true, follows));
    }

    @DeleteMapping
    public ResponseEntity<BaseResponse<Void>> unfollow(@PathVariable Long userId,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        followService.unfollow(userId, userDetails.getUser());
        return ResponseEntity.ok(BaseResponse.of("팔로우 삭제", true, null));
    }
}
