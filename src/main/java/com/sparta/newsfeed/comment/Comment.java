package com.sparta.newsfeed.comment;

import com.sparta.newsfeed.comment.dto.CommentRequestDto;
import com.sparta.newsfeed.like.commentLikes;
import com.sparta.newsfeed.like.postLikes;
import com.sparta.newsfeed.post.Post;
import com.sparta.newsfeed.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;



import java.time.ZonedDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comment_id")
	private Long id;

	@Column
	private String text;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;

	@OneToMany(mappedBy = "comment")
	private List<commentLikes> likesList;

	@Column
	private ZonedDateTime date;

	@Column
	private Integer likesCount;

	public Comment(CommentRequestDto dto, User user, Post post) {
		this.text = dto.getText();
		this.date = ZonedDateTime.now();
		this.user = user;
		this.post = post;
	}

	public void update(CommentRequestDto dto) {
		this.text = dto.getText();
	}

	public Integer countLikes() {
		this.likesCount = likesList.size();
		return this.likesCount;
	}
}
