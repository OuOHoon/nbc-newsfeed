package com.sparta.newsfeed.comment;

import com.sparta.newsfeed.comment.dto.CommentRequestDto;
import com.sparta.newsfeed.feed.Feed;
import com.sparta.newsfeed.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.ZonedDateTime;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Comment {
	@Id
	@GeneratedValue
	@Column(name = "comment_id")
	private Long comment_id;

	@Column
	private String text;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "feed_id")
	private Feed feed;

	@Column
	private ZonedDateTime date;

	public Comment(CommentRequestDto dto, User user, Feed feed) {
		this.text = dto.getText();
		this.date = ZonedDateTime.now();
		this.user = user;
		this.feed = feed;
	}
}
