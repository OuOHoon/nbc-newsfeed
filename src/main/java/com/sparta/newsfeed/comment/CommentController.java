package com.sparta.newsfeed.comment;

import com.sparta.newsfeed.comment.dto.CommentRequestDto;
import com.sparta.newsfeed.comment.dto.CommentResponseDto;
import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.post.Post;
import com.sparta.newsfeed.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/api/posts/{postId}/comments")
@RestController
@RequiredArgsConstructor
@Tag(name = "COMMENT", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "댓글 작성", description = "댓글을 작성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "댓글 작성 실패",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"댓글 작성 실패\", \"payload\": null}"
                            )
                    )
            )
    })
    public ResponseEntity<BaseResponse<CommentResponseDto>> postComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
                                                                        @AuthenticationPrincipal UserDetailsImpl user,
                                                                        @Parameter(description = "게시글 ID") @PathVariable("postId") Long postId) {
        Post post = commentService.findPostById(postId);
        CommentResponseDto dto = commentService.createComment(commentRequestDto, user.getUser(), post);
        return new ResponseEntity<>(BaseResponse.of("댓글 작성", true, dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "댓글 조회", description = "전체 댓글을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "댓글 조회 실패",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<List<CommentResponseDto>>>",
                                    value = "{\"success\": false, \"message\": \"댓글 조회 실패\", \"payload\": null}"
                            )
                    )
            )
    })
    public ResponseEntity<BaseResponse<List<CommentResponseDto>>> getAllCommentsByPost( @Parameter(description = "게시글 ID")
                                                                                        @PathVariable("postId") Long postId) {
        List<CommentResponseDto> dtos = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(BaseResponse.of("모든 댓글 조회", true, dtos));
    }

    @PutMapping("/{commentId}")
    @Operation(summary = "댓글 수정", description = "댓글을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "댓글 수정 실패",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<CommentResponseDto>>",
                                    value = "{\"success\": false, \"message\": \"댓글 수정 실패\", \"payload\": null}"
                            )
                    )
            )
    })
    public ResponseEntity<BaseResponse<CommentResponseDto>> putComment(@Parameter(description = "댓글 ID") @PathVariable Long commentId,
                                                                       @RequestBody CommentRequestDto commentRequestDto,
                                                                       @AuthenticationPrincipal UserDetailsImpl user) {
        CommentResponseDto dto = commentService.updateComment(commentId, commentRequestDto, user.getUser());
        return ResponseEntity.ok(BaseResponse.of("댓글 수정", true, dto));
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "댓글 삭제", description = "댓글을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "4xx", description = "댓글 삭제 실패",
                    content = @Content(
                            schema = @Schema(implementation = BaseResponse.class),
                            examples = @ExampleObject(
                                    name = "ResponseEntity<BaseResponse<Void>>",
                                    value = "{\"success\": false, \"message\": \"댓글 삭제 실패\", \"payload\": null}"
                            )
                    )
            )
    })
    public ResponseEntity<BaseResponse<Void>> deleteComment(@Parameter(description = "댓글 ID") @PathVariable Long commentId,
                                                            @AuthenticationPrincipal UserDetailsImpl user) {
        commentService.deleteComment(commentId, user.getUser());
        return ResponseEntity.ok(BaseResponse.of("댓글 삭제", true, null));
    }
}