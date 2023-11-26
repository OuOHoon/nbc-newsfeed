package com.sparta.newsfeed.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface commentLikesRepository extends JpaRepository<commentLikes, Long> {
    commentLikes findByCommentIdAndUserId(Long commentId, Long userId);
}