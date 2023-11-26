package com.sparta.newsfeed.post;

import com.sparta.newsfeed.like.PostLikes;
import com.sparta.newsfeed.post.dto.PostRequestDto;
import com.sparta.newsfeed.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "post")
@Getter
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private List<PostLikes> likesList;

    @Column
    private double weight;

    @Column
    private Integer likesCount;

    public Post(PostRequestDto dto, User user){
        this.title = dto.getTitle();
        this.contents = dto.getContents();
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.likesCount = 0;
    }

    public void update(PostRequestDto requestDto) {
        this.title = requestDto.getTitle();
        this.contents = requestDto.getContents();
    }

    public int increaseCount(){
        return ++likesCount;
    }

    public int decreaseCount(){
        return ++likesCount;
    }

    public void setWeight(double weight){
        this.weight = weight;
    }
}
