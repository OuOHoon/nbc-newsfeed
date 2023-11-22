package com.sparta.newsfeed.feed;

import com.sparta.newsfeed.Timestamped;
import com.sparta.newsfeed.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Feed")
@Getter
@NoArgsConstructor
public class Feed extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feed_id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Feed(FeedRequestDto dto, User user){
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.user = user;
    }

    public void update(FeedRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }
}
