package com.sparta.newsfeed.user;

import com.sparta.newsfeed.feed.Feed;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @OneToMany(mappedBy = "user")
    private List<Feed> feedList = new ArrayList<>();
}
