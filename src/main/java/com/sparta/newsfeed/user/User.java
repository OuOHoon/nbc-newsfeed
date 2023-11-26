package com.sparta.newsfeed.user;

import com.sparta.newsfeed.like.PostLikes;
import com.sparta.newsfeed.profile.Profile;
import com.sparta.newsfeed.user.follow.Follow;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @OneToMany(mappedBy = "user") // 이 유저 id가 user_id인 follow 관계를 찾음
    private List<Follow> followingFollows = new ArrayList<>();

    @OneToMany(mappedBy = "followUser") // 이 유저 id가 follow_user_id인 follow 관계를 모두 찾음
    private List<Follow> followerFollows = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PostLikes> postLikesList;

    public User(String username, String password, UserRoleEnum role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRoleEnum role) {
        this.role = role;
    }
}
