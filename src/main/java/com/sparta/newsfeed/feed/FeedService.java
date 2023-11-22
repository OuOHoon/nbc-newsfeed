package com.sparta.newsfeed.feed;

import com.sparta.newsfeed.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
public class FeedService {

    private FeedRepository feedRepository;

    public FeedResponseDto createFeed(FeedRequestDto requestDto, User user) {
        Feed feed = feedRepository.save(new Feed(requestDto, user));
        return new FeedResponseDto(feed);
    }

    public Page<FeedResponseDto> getAllFeeds(@RequestParam("page") int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "CreatedAt");
        Pageable pageable = PageRequest.of(page, 30, sort);
        Page<Feed> feedList = feedRepository.findAllByOrderByCreatedAtDesc(pageable);
        return feedList.map(FeedResponseDto::new);
    }

    public FeedResponseDto getFeed(Long id) {
        Feed feed = findFeed(id);
        return new FeedResponseDto(feed);
    }

    @Transactional
    public FeedResponseDto updateFeed(Long id, FeedRequestDto requestDto, User user) {
        Feed feed = findFeed(id);
        checkUser(feed, user);
        feed.update(requestDto);
        return new FeedResponseDto(feed);
    }

    @Transactional
    public void deleteFeed(Long id, User user) {
        Feed feed = findFeed(id);
        checkUser(feed, user);
        feedRepository.delete(feed);
    }

    private Feed findFeed(Long id) {
        return feedRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 피드는 존재하지 않습니다.")
        );
    }

    private void checkUser(Feed feed, User user) {
        if (!feed.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자만 수정/삭제 가능합니다.");
        }
    }
}
