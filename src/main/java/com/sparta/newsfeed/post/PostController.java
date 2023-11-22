package com.sparta.newsfeed.post;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.security.UserDetailsImpl;
import jakarta.validation.Valid;
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
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<RedirectView> createFeed(@RequestBody @Valid PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long postId = postService.createFeed(requestDto, userDetails.getUser());
        RedirectView redirectView = new RedirectView("/api/posts/" + postId);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY.value()).body(redirectView);
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<PostResponseDto>>> getAllFeeds(@RequestParam("page") int page){
        Page<PostResponseDto> feedList = postService.getAllFeeds(page);
        return ResponseEntity.ok(BaseResponse.of("뉴스피드 페이지", HttpStatus.OK.value(), feedList));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<BaseResponse<PostResponseDto>> getFeed(@PathVariable Long postId){
        PostResponseDto dto = postService.getFeed(postId);
        return ResponseEntity.ok(BaseResponse.of("선택 Feed 조회", HttpStatus.OK.value(), dto));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<RedirectView> updateFeed(@PathVariable Long postId, @RequestBody @Valid PostRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.updateFeed(postId, requestDto, userDetails.getUser());
        RedirectView redirectView = new RedirectView("/api/posts/" + postId);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY.value()).body(redirectView);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<RedirectView> deleteFeed(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.deleteFeed(postId, userDetails.getUser());
        RedirectView redirectView = new RedirectView("/api/posts");
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY.value()).body(redirectView);
    }
}