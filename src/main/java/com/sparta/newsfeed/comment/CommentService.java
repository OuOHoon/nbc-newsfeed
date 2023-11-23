package com.sparta.newsfeed.comment;

import com.sparta.newsfeed.comment.dto.CommentRequestDto;
import com.sparta.newsfeed.comment.dto.CommentResponseDto;
import com.sparta.newsfeed.post.Post;
import com.sparta.newsfeed.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	// C
	@Transactional
	public CommentResponseDto createComment(CommentRequestDto dto, User user, Post post) {
		Comment comment = new Comment(dto, user, post);
		Comment savedComment = commentRepository.save(comment);
		return new CommentResponseDto(savedComment);
	}

	// R
//	public CommentResponseDto getComment(Long commentId) {
//		Comment comment = commentRepository.findById(commentId)
//				.orElseThrow(() -> new EntityNotFoundException("해당 댓글이 존재하지 않습니다."));
//		return new CommentResponseDto(comment);
//	}

	public List<CommentResponseDto> getCommentsByPost(Long postId) {
		List<Comment> comments = commentRepository.findByPostId(postId);
		return comments.stream()
				.map(CommentResponseDto::new)
				.collect(Collectors.toList());
	}

	// U
	@Transactional
	public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
		if (!comment.getUser().equals(user)) {
			throw new IllegalStateException("댓글 수정 권한이 없습니다.");
		}

		comment.update(commentRequestDto);
		return new CommentResponseDto(comment);
	}
	// D
	@Transactional
	public void deleteComment(Long commentId, User user) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
		if (!comment.getUser().equals(user)) {
			throw new EntityNotFoundException("댓글 삭제 권한이 없습니다.");
		}
		commentRepository.delete(comment);
	}
}