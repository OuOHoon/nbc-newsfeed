package com.sparta.newsfeed.post;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.post.dto.PostRequestDto;
import com.sparta.newsfeed.post.dto.PostResponseDto;
import com.sparta.newsfeed.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@Tag(name = "POST", description = "게시글 API")
@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @Operation(summary = "포스트 작성")
    @ApiResponse(responseCode = "301", description = "작성된 포스트 url로 새로고침", headers = {@Header(name = "location", description = "/api/posts/{postId}")})
    @PostMapping
    public ResponseEntity<RedirectView> createPost(@Valid @RequestBody PostRequestDto requestDto,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        Long postId = postService.createPost(requestDto, userDetails.getUser());
        RedirectView redirectView = new RedirectView("/api/posts/" + postId);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY.value()).body(redirectView);
    }

    @Operation(summary = "뉴스피드 페이지", description = "모든 포스트를 팔로우, 좋아요, 작성일에 따라 계산된 가중치로 정렬하여 조회")
    @ApiResponse(responseCode = "200", description = "뉴스피드 페이지 조회")
    @Parameters({
            @Parameter(description = "페이지 번호", name = "page"),
            @Parameter(description = "한 페이지의 포스트 수", name = "size")
    })
    @GetMapping
    public ResponseEntity<BaseResponse<Page<PostResponseDto>>> getAllPosts(@RequestParam("page") int page,
                                                                           @RequestParam("size") int size,
                                                                           @AuthenticationPrincipal UserDetailsImpl userDetails){
        Page<PostResponseDto> postList = postService.getAllPosts(page - 1, size, userDetails);
        return ResponseEntity.ok(BaseResponse.of("뉴스피드 페이지", true, postList));
    }

    @GetMapping("/{postId}")
    @Operation(summary = "포스트 조회")
    public ResponseEntity<BaseResponse<PostResponseDto>> getPost(@PathVariable Long postId){
        PostResponseDto dto = postService.getPost(postId);
        return ResponseEntity.ok(BaseResponse.of("선택 포스트 조회", true, dto));
    }

    @PutMapping("/{postId}")
    public ResponseEntity<RedirectView> updatePost(@PathVariable Long postId,
                                                   @Valid @RequestBody PostRequestDto requestDto,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.updatePost(postId, requestDto, userDetails.getUser());
        RedirectView redirectView = new RedirectView("/api/posts/" + postId);
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY.value()).body(redirectView);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<RedirectView> deletePost(@PathVariable Long postId,
                                                   @AuthenticationPrincipal UserDetailsImpl userDetails){
        postService.deletePost(postId, userDetails.getUser());
        RedirectView redirectView = new RedirectView("/api/posts");
        return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY.value()).body(redirectView);
    }
}