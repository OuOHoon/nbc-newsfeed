package com.sparta.newsfeed.feed;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public ResponseEntity<BaseResponse<Page<FeedResponseDto>>> getAllFeeds(@RequestParam("page") int page){
        Page<FeedResponseDto> feedList = feedService.getAllFeeds(page);
        return ResponseEntity.ok(BaseResponse.of("뉴스피드 페이지", 201, feedList));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<FeedResponseDto>> getFeed(@PathVariable Long postId){
        FeedResponseDto dto = feedService.getFeed(postId);
        return ResponseEntity.ok(BaseResponse.of("선택 Feed 조회", 201, dto));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<BaseResponse<FeedResponseDto>> updateFeed(@PathVariable Long postId, @RequestBody FeedRequestDto requestDto, User user){
        FeedResponseDto dto = feedService.updateFeed(postId, requestDto, user);
        return ResponseEntity.ok(BaseResponse.of("선택 Feed 수정", 201, dto));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<BaseResponse<Void>> deleteFeed(@PathVariable Long postId, User user){
        feedService.deleteFeed(postId, user);
        return ResponseEntity.ok(BaseResponse.of("선택 Feed 삭제", 201, null));
    }
}