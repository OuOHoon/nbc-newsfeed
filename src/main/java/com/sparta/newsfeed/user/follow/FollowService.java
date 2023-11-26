package com.sparta.newsfeed.user.follow;

import com.sparta.newsfeed.common.exception.follow.SelfFollowException;
import com.sparta.newsfeed.common.exception.user.NotFoundUserException;
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
        // follow할 userId 찾을 수 없다면 예외 발생
        User followingUser = userRepository.findById(followUserId).orElseThrow(NotFoundUserException::new);

        // 자기 자신을 팔로우 하면 안됨
        if (isSameUser(followUserId, user)) {
            throw new SelfFollowException();
        }

        Optional<Follow> optionalFollow = followRepository
                .findByUserIdAndFollowUserId(user.getId(), followingUser.getId());
        // 이미 팔로우 관계가 존재한다면 예외 발생
        if (optionalFollow.isPresent()) {
            throw new DuplicateFollowException();
        }
        Follow follow = new Follow();
        follow.setFollow(user, followingUser);
        followRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long unfollowUserId, User user) {
        // 언팔할 유저 id로 유저를 찾을 수 없다면 예외 발생
        User unfollowUser = userRepository.findById(unfollowUserId).orElseThrow(NotFoundUserException::new);
        // 팔로우 한 기록이 없다면 예외 발생
        Follow follow = followRepository.findByUserIdAndFollowUserId(user.getId(), unfollowUser.getId())
                .orElseThrow(NotFoundFollowException::new);
        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public FollowResponseDto getFollows(Long userId) {
        // 유저를 찾을 수 없다면 예외 발생
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        log.info("follower 조회 쿼리");
        List<String> followers = user.getFollowerFollows().stream()
                .map((follow) -> follow.getUser().getProfile().getNickname()).toList();
        log.info("following 조회 쿼리");
        List<String> followings = user.getFollowingFollows().stream()
                .map((follow) -> follow.getFollowUser().getProfile().getNickname()).toList();
        return new FollowResponseDto(followers, followings);
    }

    private boolean isSameUser(Long userId, User user) {
        return userId.equals(user.getId());
    }
}
