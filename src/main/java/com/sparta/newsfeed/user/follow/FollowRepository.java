package com.sparta.newsfeed.user.follow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Optional<Follow> findByUserIdAndFollowUserId(Long userId, Long followUserId);
}
