package com.sparta.newsfeed.profile;

import com.sparta.newsfeed.common.exception.user.InvalidUserException;
import com.sparta.newsfeed.common.exception.user.NotFoundUserException;
import com.sparta.newsfeed.profile.dto.ProfileRequestDto;
import com.sparta.newsfeed.profile.dto.ProfileResponseDto;
import com.sparta.newsfeed.user.User;
import com.sparta.newsfeed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;

    public ProfileResponseDto findProfile(Long userId) {
        return toResponseDto(profileRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new));
    }

    public ProfileResponseDto createProfile(Long userId, ProfileRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        Profile profile = Profile.builder()
                .nickname(request.getNickname())
                .introduction(request.getIntroduction())
                .build();
        profile.setUser(user);
        profileRepository.save(profile);
        return toResponseDto(profile);
    }

    @Transactional
    public ProfileResponseDto updateProfile(Long userId, String username, ProfileRequestDto request) {
        Profile profile = profileRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        if (!profile.getUser().getUsername().equals(username)) {
            throw new InvalidUserException();
        }
        profile.update(request.getNickname(), request.getIntroduction());
        return toResponseDto(profile);
    }

    public static ProfileResponseDto toResponseDto(Profile profile) {
        return ProfileResponseDto.builder()
                .nickname(profile.getNickname())
                .introduction(profile.getIntroduction())
                .build();
    }
}
