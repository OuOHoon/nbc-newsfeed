package com.sparta.newsfeed.feed;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedRepository extends JpaRepository<Feed, Long> {
    List<Feed> findAllByOrderByCreatedAtDesc();
}
