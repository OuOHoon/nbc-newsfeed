package com.sparta.newsfeed.like;

import com.sparta.newsfeed.post.Post;
import com.sparta.newsfeed.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "postLikes")
@Getter
@NoArgsConstructor
public class postLikes {
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

    public postLikes(Post post, User user){
        this.post = post;
        this.user = user;
    }
}
