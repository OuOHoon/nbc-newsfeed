package com.sparta.newsfeed.feed;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.user.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @PostMapping
    public ResponseEntity<RedirectView> createFeed(@RequestBody FeedRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long postId = feedService.createFeed(requestDto, userDetails.getUser());
        RedirectView redirectView = new RedirectView("/api/posts/" + postId);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY.value()).body(redirectView);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<FeedResponseDto>>> getAllFeeds(@RequestParam("page") int page){
        Page<FeedResponseDto> feedList = feedService.getAllFeeds(page);
        return ResponseEntity.ok(BaseResponse.of("뉴스피드 페이지", HttpStatus.OK.value(), feedList));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<FeedResponseDto>> getFeed(@PathVariable Long postId){
        FeedResponseDto dto = feedService.getFeed(postId);
        return ResponseEntity.ok(BaseResponse.of("선택 Feed 조회", HttpStatus.OK.value(), dto));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<RedirectView> updateFeed(@PathVariable Long postId, @RequestBody FeedRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        feedService.updateFeed(postId, requestDto, userDetails.getUser());
        RedirectView redirectView = new RedirectView("/api/posts/" + postId);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY.value()).body(redirectView);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<RedirectView> deleteFeed(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        feedService.deleteFeed(postId, userDetails.getUser());
        RedirectView redirectView = new RedirectView("/api/posts");
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY.value()).body(redirectView);
    }
}