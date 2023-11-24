package com.sparta.newsfeed.like;

import com.sparta.newsfeed.comment.Comment;
import com.sparta.newsfeed.post.Post;
import com.sparta.newsfeed.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "likes")
@Getter
@NoArgsConstructor
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    public Likes(Post post, User user){
        this.post = post;
        this.user = user;
    }

    public Likes(Comment comment, User user){
        this.comment = comment;
        this.user = user;
    }

}
