package com.sparta.newsfeed;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class user {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
