package com.sparta.newsfeed.user;

import com.sparta.newsfeed.like.postLikes;
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

    @OneToMany(mappedBy = "user")
    private List<Follow> followingFollows = new ArrayList<>();

    @OneToMany(mappedBy = "followUser")
    private List<Follow> followerFollows = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<postLikes> postLikesList;

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
