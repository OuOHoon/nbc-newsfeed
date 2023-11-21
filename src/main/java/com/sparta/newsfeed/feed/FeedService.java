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
}
