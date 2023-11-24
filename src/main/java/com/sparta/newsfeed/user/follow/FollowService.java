package com.sparta.newsfeed.user.follow;

import com.sparta.newsfeed.user.NotFoundUserException;
import com.sparta.newsfeed.user.User;
import com.sparta.newsfeed.user.UserRepository;
import com.sparta.newsfeed.user.follow.dto.FollowResponseDto;
import com.sparta.newsfeed.user.follow.exception.DuplicateFollowException;
import com.sparta.newsfeed.user.follow.exception.NotFoundFollowException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class FollowService {

    private final UserRepository userRepository;

    private final FollowRepository followRepository;


    @Transactional
    public void follow(Long followUserId, User user) {
        userRepository.save(user);
        User followingUser = userRepository.findById(followUserId).orElseThrow(NotFoundUserException::new);
        Optional<Follow> optionalFollow = followRepository
                .findByUserIdAndFollowUserId(user.getId(), followingUser.getId());
        if (optionalFollow.isPresent()) {
            throw new DuplicateFollowException();
        }
        Follow follow = new Follow();
        follow.setFollow(user, followingUser);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long unfollowUserId, User user) {
        User unfollowUser = userRepository.findById(unfollowUserId).orElseThrow(NotFoundUserException::new);
        Follow follow = followRepository.findByUserIdAndFollowUserId(user.getId(), unfollowUser.getId())
                .orElseThrow(NotFoundFollowException::new);
        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public FollowResponseDto getFollows(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        log.info("follower 조회 쿼리");
        List<String> followers = user.getFollowerFollows().stream()
                .map((follow) -> follow.getFollowUser().getUsername()).toList();
        log.info("following 조회 쿼리");
        List<String> followings = user.getFollowingFollows().stream()
                .map((follow) -> follow.getFollowUser().getUsername()).toList();
        return new FollowResponseDto(followers, followings);
    }
}
