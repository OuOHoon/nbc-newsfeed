package com.sparta.newsfeed.post;

import com.sparta.newsfeed.common.exception.post.NotFoundPostException;
import com.sparta.newsfeed.common.exception.post.OnlyAuthorAccessException;
import com.sparta.newsfeed.post.dto.PostRequestDto;
import com.sparta.newsfeed.post.dto.PostResponseDto;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.user.User;
import com.sparta.newsfeed.user.UserRoleEnum;
import com.sparta.newsfeed.user.follow.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final FollowRepository followRepository;

    private static final double FOLLOW = 0.5;
    private static final double LIKE = 0.4;
    private static final double DATE = 0.1;

    public Long createPost(PostRequestDto requestDto, User user) {
        Post post = postRepository.save(new Post(requestDto, user));
        return post.getId();
    }

    @Transactional
    public Page<PostResponseDto> getAllPosts(@RequestParam("page") int page,
                                             @RequestParam("size") int size,
                                             UserDetailsImpl userDetails) {

        // 유저 조회인 경우
        if (userDetails.getUser().getRole().equals(UserRoleEnum.USER)) {
            calculateWeightForUser(userDetails.getUser());
        } else { // 게스트 조회인 경우
            calculateWeightForGuests();
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "weight");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Post> postList = postRepository.findAll(pageable);
        return postList.map(PostResponseDto::new);
    }

    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundPostException::new);
        return new PostResponseDto(post);
    }

    @Transactional
    public void updatePost(Long id, PostRequestDto requestDto, User user) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundPostException::new);
        checkUser(post, user);
        post.update(requestDto);
    }

    @Transactional
    public void deletePost(Long id, User user) {
        Post post = postRepository.findById(id).orElseThrow(NotFoundPostException::new);
        checkUser(post, user);
        postRepository.delete(post);
    }

    private void checkUser(Post post, User user) {
        if (!post.getUser().getId().equals(user.getId())) {
            throw new OnlyAuthorAccessException();
        }
    }

    @Transactional
    public void calculateWeightForGuests() {
        // 게스트 유저 피드 정렬 기준 가중치 = 좋아요(0.8) + 작성일(0.2)

        double maxLikes = postRepository.findTopByOrderByLikesCountDesc().orElseThrow(NotFoundPostException::new)
                .getLikesCount();

        for (Post post : postRepository.findAll()) {
            double likeScore = post.getLikesCount() / maxLikes * LIKE * 2;

            LocalDateTime now = LocalDateTime.now();
            long daysBetween = ChronoUnit.DAYS.between(post.getCreatedAt(), now);
            double dateScore = DATE * Math.exp(-daysBetween) * 2;

            post.setWeight(likeScore + dateScore);
        }
    }

    @Transactional
    public void calculateWeightForUser(User user) {
        // 로그인 유저 피드 정렬 기준 가중치 = 팔로우(0.5) + 좋아요(0.4) + 작성일(0.1)

        double maxLikes = postRepository.findTopByOrderByLikesCountDesc().orElseThrow(NotFoundPostException::new)
                .getLikesCount();

        for (Post post : postRepository.findAll()) {
            double followScore = 0;
            if (followRepository.findByUserIdAndFollowUserId(user.getId(), post.getUser().getId()).isPresent()) {
                followScore = FOLLOW;
            }

            double likeScore = post.getLikesCount() / maxLikes * LIKE;

            LocalDateTime now = LocalDateTime.now();
            long daysBetween = ChronoUnit.DAYS.between(post.getCreatedAt(), now);
            double dateScore = DATE * Math.exp(-daysBetween);

            post.setWeight(followScore + likeScore + dateScore);
        }
    }
}
