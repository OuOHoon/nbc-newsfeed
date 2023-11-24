package com.sparta.newsfeed.comment;

import com.sparta.newsfeed.comment.dto.CommentRequestDto;
import com.sparta.newsfeed.comment.dto.CommentResponseDto;
import com.sparta.newsfeed.comment.exception.NoPrivilegesException;
import com.sparta.newsfeed.comment.exception.NotFoundCommentException;
import com.sparta.newsfeed.comment.exception.NotFoundPostException;
import com.sparta.newsfeed.post.Post;
import com.sparta.newsfeed.post.PostRepository;
import com.sparta.newsfeed.user.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

	private final CommentRepository commentRepository;
	private final PostRepository postRepository;

	@Valid
	@Transactional
	public CommentResponseDto createComment(CommentRequestDto dto, User user, Post post) {

		Comment comment = new Comment(dto, user, post);
		Comment savedComment = commentRepository.save(comment);

		return new CommentResponseDto(savedComment);
	}

	public List<CommentResponseDto> getCommentsByPost(Long postId) {

		List<Comment> comments = commentRepository
									.findByPostId(postId);

		return comments.stream().map(CommentResponseDto::new)
						.collect(Collectors.toList());
	}


	@Transactional
	public CommentResponseDto updateComment(Long commentId
											, CommentRequestDto commentRequestDto
											, User user) {

		Comment comment = commentRepository.findById(commentId)
											.orElseThrow(NotFoundCommentException::new);
		checkUser(comment, user);
		comment.update(commentRequestDto);

		return new CommentResponseDto(comment);
	}

	@Transactional
	public void deleteComment(Long commentId, User user) {

		Comment comment = commentRepository.findById(commentId)
											.orElseThrow(NotFoundCommentException::new);
		checkUser(comment, user);
		commentRepository.delete(comment);
	}

	public Post findPostById(Long postId) {

		return postRepository.findById(postId)
				.orElseThrow(NotFoundPostException::new);
	}

	private void checkUser(Comment comment, User user) {
		if (!comment.getUser().getId().equals(user.getId())) {
			throw new NoPrivilegesException();
		}
	}

}