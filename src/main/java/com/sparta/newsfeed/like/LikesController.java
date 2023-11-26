package com.sparta.newsfeed.like;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "LIKE", description = "좋아요 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}")
public class LikesController {

    private final LikesService likesService;

    @Operation(summary = "포스트 좋아요")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요가 완료 되었습니다"),
            @ApiResponse(responseCode = "4XX", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    @PostMapping("/likes")
    public ResponseEntity<BaseResponse<LikesResponseDto>> likePost(@PathVariable Long postId,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        LikesResponseDto dto = new LikesResponseDto(likesService.likePost(postId, userDetails.getUser()));
        return ResponseEntity.ok(BaseResponse.of("좋아요가 완료 되었습니다.", true, dto));
    }

    @Operation(summary = "포스트 좋아요 취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요가 취소 되었습니다"),
            @ApiResponse(responseCode = "400", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    @DeleteMapping("/likes")
    public ResponseEntity<BaseResponse<LikesResponseDto>> unlikePost(@PathVariable Long postId,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        LikesResponseDto dto = new LikesResponseDto(likesService.unlikePost(postId, userDetails.getUser()));
        return ResponseEntity.ok(BaseResponse.of("좋아요가 취소 되었습니다.", true, dto));
    }

    @Operation(summary = "댓글 좋아요")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요가 완료 되었습니다"),
            @ApiResponse(responseCode = "4XX", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<BaseResponse<LikesResponseDto>> likeComment(@PathVariable Long postId,
                                                                   @PathVariable Long commentId,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        LikesResponseDto dto = new LikesResponseDto(likesService.likeComment(commentId, userDetails.getUser()));
        return ResponseEntity.ok(BaseResponse.of("좋아요가 완료 되었습니다.", true, dto));
    }

    @Operation(summary = "댓글 좋아요 취소")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요가 취소 되었습니다"),
            @ApiResponse(responseCode = "4XX", description = "에러 메세지",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"에러 메세지\", \"payload\": null}"
                            )
                    ))
    })
    @DeleteMapping("/comments/{commentId}/likes")
    public ResponseEntity<BaseResponse<LikesResponseDto>> unlikeComment(@PathVariable Long postId,
                                                                      @PathVariable Long commentId,
                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails){
        LikesResponseDto dto = new LikesResponseDto(likesService.unlikeComment(commentId, userDetails.getUser()));
        return ResponseEntity.ok(BaseResponse.of("좋아요가 취소 되었습니다.", true, dto));
    }
}
