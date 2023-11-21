package com.sparta.newsfeed.feed;

import com.sparta.newsfeed.user.User;
import org.springframework.stereotype.Service;

@Service
public class FeedService {

    private FeedRepository feedRepository;

    public FeedResponseDto createFeed(FeedRequestDto requestDto, User user) {
        Feed feed = feedRepository.save(new Feed(requestDto, user));
        return new FeedResponseDto(feed);
    }

    public FeedResponseDto getFeed(Long postId) {
        Feed feed = findFeed(postId);
        return new FeedResponseDto(feed);
    }

    private Feed findFeed(Long id) {
        return feedRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 피드는 존재하지 않습니다.")
        );
    }
}
