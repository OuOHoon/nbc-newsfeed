package com.sparta.newsfeed.post;

import com.sparta.newsfeed.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public Long createFeed(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user));
        return post.getId();
    }

    @Transactional(readOnly = true)
    public Page<PostResponseDto> getAllFeeds(@RequestParam("page") int page) {
        Sort sort = Sort.by(Sort.Direction.DESC, "CreatedAt");
        Pageable pageable = PageRequest.of(page, 30, sort);
        Page<Post> feedList = postRepository.findAllByOrderByCreatedAtDesc(pageable);
        return feedList.map(PostResponseDto::new);
    }

    public PostResponseDto getFeed(Long id) {
        Post post = findFeed(id);
        return new PostResponseDto(post);
    }

    @Transactional
    public void updateFeed(Long id, PostRequestDto requestDto, User user) {
        Post post = findFeed(id);
        checkUser(post, user);
        post.update(requestDto);
    }

    @Transactional
    public void deleteFeed(Long id, User user) {
        Post post = findFeed(id);
        checkUser(post, user);
        postRepository.delete(post);
    }

    private Post findFeed(Long id) {
        return postRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("해당 피드는 존재하지 않습니다.")
        );
    }

    private void checkUser(Post post, User user) {
        if (!post.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("작성자만 수정/삭제 가능합니다.");
        }
    }
}
