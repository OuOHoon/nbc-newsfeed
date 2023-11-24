package com.sparta.newsfeed.like;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}")
public class LikesController {

    private final LikesService likesService;

    @PostMapping("/likes")
    public ResponseEntity<BaseResponse<LikesResponseDto>> likePost(@PathVariable Long postId,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        LikesResponseDto dto = (new LikesResponseDto(likesService.likePost(postId, userDetails.getUser())));
        return ResponseEntity.ok(BaseResponse.of("좋아요가 완료 되었습니다.", HttpStatus.OK.value(), dto));
    }

    @DeleteMapping("/likes")
    public ResponseEntity<BaseResponse<LikesResponseDto>> unlikePost(@PathVariable Long postId,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        LikesResponseDto dto = (new LikesResponseDto(likesService.unlikePost(postId, userDetails.getUser())));
        return ResponseEntity.ok(BaseResponse.of("좋아요가 취소 되었습니다.", HttpStatus.OK.value(), dto));
    }

    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<BaseResponse<LikesResponseDto>> likeComment(@PathVariable Long postId,
                                                                   @PathVariable Long commentId,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        LikesResponseDto dto = (new LikesResponseDto(likesService.likeComment(commentId, userDetails.getUser())));
        return ResponseEntity.ok(BaseResponse.of("좋아요가 완료 되었습니다.", HttpStatus.OK.value(), dto));
    }

    @DeleteMapping("/comments/{commentId}/likes")
    public ResponseEntity<BaseResponse<LikesResponseDto>> unlikeComment(@PathVariable Long postId,
                                                                      @PathVariable Long commentId,
                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        LikesResponseDto dto = (new LikesResponseDto(likesService.unlikeComment(commentId, userDetails.getUser())));
        return ResponseEntity.ok(BaseResponse.of("좋아요가 취소 되었습니다.", HttpStatus.OK.value(), dto));
    }
}
