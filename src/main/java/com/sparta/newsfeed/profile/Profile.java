package com.sparta.newsfeed.profile;

import com.sparta.newsfeed.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "profile_id")
    private Long id;

    @Column(length = 255)
    private String nickname;

    @Column(length = 255)
    private String introduction;

    private String imageUrl;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public void update(String nickname, String introduction) {
        this.nickname = nickname;
        this.introduction = introduction;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
