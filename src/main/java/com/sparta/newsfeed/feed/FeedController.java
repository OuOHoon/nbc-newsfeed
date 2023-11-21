package com.sparta.newsfeed.feed;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @PostMapping
    public ResponseEntity<BaseResponse<FeedResponseDto>> createFeed(@RequestBody FeedRequestDto requestDto, User user){
        FeedResponseDto dto = feedService.createFeed(requestDto, user);
        return ResponseEntity.ok(BaseResponse.of("Feed 작성", 201, dto));
    }
}