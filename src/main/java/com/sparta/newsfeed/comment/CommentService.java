package com.sparta.newsfeed.comment;

import com.sparta.newsfeed.comment.dto.CommentRequestDto;
import com.sparta.newsfeed.comment.dto.CommentResponseDto;
import com.sparta.newsfeed.feed.Feed;
import com.sparta.newsfeed.user.User;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	// C
	@Transactional
	public CommentResponseDto createComment(CommentRequestDto dto, User user, Feed feed) {
		Comment comment = new Comment();

		comment.setFeed(feed);
		comment.setUser(user);
		comment.setText(dto.getText());

		Comment savedComment = commentRepository.save(comment);
		return new CommentResponseDto(savedComment);
	}

	// R
	public CommentResponseDto getComment(Long commentId) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new EntityNotFoundException("해당 댓글이 존재하지 않습니다."));
		return new CommentResponseDto(comment);
	}

	// U
	@Transactional
	public CommentResponseDto updateComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
		if (!comment.getUser().equals(user)) {
			throw new IllegalStateException("댓글 수정 권한이 없습니다.");
		}
		comment.setText(commentRequestDto.getText());
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
