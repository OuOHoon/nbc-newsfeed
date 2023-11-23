package com.sparta.newsfeed.like;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/likes")
public class LikesController {

    private final LikesService likesService;

    @PostMapping
    public ResponseEntity<BaseResponse<LikesResponseDto>> likePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Boolean isLiked = likesService.likePost(postId, userDetails.getUser());
        LikesResponseDto dto = (new LikesResponseDto(likesService.countLikes(postId)));

        if(isLiked){
            return ResponseEntity.ok(BaseResponse.of("좋아요가 완료 되었습니다.", HttpStatus.OK.value(), dto));
        } else{
            return ResponseEntity.ok(BaseResponse.of("좋아요가 취소 되었습니다.", HttpStatus.OK.value(), dto));
        }
    }
}
