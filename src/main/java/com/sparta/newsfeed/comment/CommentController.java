package com.sparta.newsfeed.comment;

import com.sparta.newsfeed.comment.dto.CommentRequestDto;
import com.sparta.newsfeed.comment.dto.CommentResponseDto;
import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.post.Post;
import com.sparta.newsfeed.security.UserDetailsImpl;
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
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<BaseResponse<CommentResponseDto>> postComment(@Valid @RequestBody CommentRequestDto commentRequestDto,
                                                                        @AuthenticationPrincipal UserDetailsImpl user,
                                                                        @PathVariable("postId") Long postId) {
        Post post = commentService.findPostById(postId);
        CommentResponseDto dto = commentService.createComment(commentRequestDto, user.getUser(), post);
        return new ResponseEntity<>(BaseResponse.of("댓글 작성", true, dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<CommentResponseDto>>> getAllCommentsByPost(@PathVariable("postId") Long postId) {
        List<CommentResponseDto> dtos = commentService.getCommentsByPost(postId);
        return ResponseEntity.ok(BaseResponse.of("모든 댓글 조회", true, dtos));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<BaseResponse<CommentResponseDto>> putComment(@PathVariable Long commentId,
                                                                       @RequestBody CommentRequestDto commentRequestDto,
                                                                       @AuthenticationPrincipal UserDetailsImpl user) {
        CommentResponseDto dto = commentService.updateComment(commentId, commentRequestDto, user.getUser());
        return ResponseEntity.ok(BaseResponse.of("댓글 수정", true, dto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<BaseResponse<Void>> deleteComment(@PathVariable Long commentId,
                                                            @AuthenticationPrincipal UserDetailsImpl user) {
        commentService.deleteComment(commentId, user.getUser());
        return ResponseEntity.ok(BaseResponse.of("댓글 삭제", true, null));
    }
}