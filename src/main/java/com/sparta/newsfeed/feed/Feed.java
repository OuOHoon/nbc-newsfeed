package com.sparta.newsfeed.feed;

import com.sparta.newsfeed.Timestamped;
import com.sparta.newsfeed.user.User;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Feed")
@NoArgsConstructor
public class Feed extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Feed(FeedRequestDto dto, User user){
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.user = user;
    }
}
