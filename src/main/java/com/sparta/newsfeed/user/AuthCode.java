package com.sparta.newsfeed.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "authcode")
public class AuthCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private int code;

    @Column(nullable = false)
    private final LocalDateTime createdAt = LocalDateTime.now();

    public AuthCode(String username, int code) {
        this.username = username;
        this.code = code;
    }

}
