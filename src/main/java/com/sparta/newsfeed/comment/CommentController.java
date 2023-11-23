package com.sparta.newsfeed.comment;

import com.sparta.newsfeed.comment.dto.CommentRequestDto;
import com.sparta.newsfeed.comment.dto.CommentResponseDto;
import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.feed.Feed;
import com.sparta.newsfeed.feed.FeedRepository;
import com.sparta.newsfeed.profile.ProfileService;
import com.sparta.newsfeed.profile.dto.ProfileRequestDto;
import com.sparta.newsfeed.profile.dto.ProfileResponseDto;
import com.sparta.newsfeed.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.util.concurrent.RejectedExecutionException;


@RequestMapping("/api/posts/{postId}/comments")
@RestController
@RequiredArgsConstructor
public class CommentController {

	private final CommentService commentService;
	private final FeedRepository feedRepository;

	@GetMapping("/{commentId}")
	public ResponseEntity<BaseResponse<CommentResponseDto>> findCommentsByPost(@PathVariable("postId") Long postId) {
		CommentResponseDto dto = commentService.getComment(postId);
		return ResponseEntity.ok(BaseResponse.of("post의 comments 조회", HttpStatus.OK.value(), dto));
	}

	@PostMapping
	public ResponseEntity<BaseResponse<CommentResponseDto>> postComment(@Valid @RequestBody CommentRequestDto commentRequestDto
																,@AuthenticationPrincipal UserDetailsImpl user
																		, @PathVariable("postId") Long postId) {

		Feed feed = feedRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException(" 피드 에러 "));

		CommentResponseDto dto = commentService.createComment(commentRequestDto, user.getUser(), feed);
		return new ResponseEntity<>(BaseResponse.of("댓글 작성", HttpStatus.OK.value(), dto), HttpStatus.CREATED);
	}

	@PutMapping("/{commentId}")
	public ResponseEntity<BaseResponse<CommentResponseDto>> putComment(@PathVariable Long commentId, @RequestBody CommentRequestDto commentRequestDto
																		,@AuthenticationPrincipal UserDetailsImpl user) {
		CommentResponseDto dto = commentService.updateComment(commentId, commentRequestDto, user.getUser());
		return ResponseEntity.ok(BaseResponse.of("댓글 수정", HttpStatus.OK.value(), dto));
	}


	@DeleteMapping
	public ResponseEntity<BaseResponse<String>> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserDetailsImpl user) {
		commentService.deleteComment(commentId, user.getUser());
		return ResponseEntity.ok(BaseResponse.of("댓글 삭제", HttpStatus.OK.value(), "댓글이 삭제되었습니다."));
	}


}
