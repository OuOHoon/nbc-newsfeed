package com.sparta.newsfeed.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface postLikesRepository extends JpaRepository<postLikes, Long> {
    postLikes findByPostIdAndUserId(Long postId, Long userId);
}
