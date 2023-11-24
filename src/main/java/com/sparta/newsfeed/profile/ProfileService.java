package com.sparta.newsfeed.profile;

import com.sparta.newsfeed.common.S3Uploader;
import com.sparta.newsfeed.profile.dto.ProfileRequestDto;
import com.sparta.newsfeed.profile.dto.ProfileResponseDto;
import com.sparta.newsfeed.common.exception.InvalidUserException;
import com.sparta.newsfeed.common.exception.NotFoundUserException;
import com.sparta.newsfeed.user.User;
import com.sparta.newsfeed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final S3Uploader s3Uploader;

    public ProfileResponseDto findProfile(Long userId) {
        return toResponseDto(profileRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new));
    }

    public ProfileResponseDto createProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundUserException::new);
        Profile profile = Profile.builder()
                .nickname(RandomStringUtils.random(10, true, true))
                .introduction("")
                .build();
        profile.setUser(user);
        profileRepository.save(profile);
        return toResponseDto(profile);
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

    @Transactional
    public String uploadProfileImage(Long userId, User user, MultipartFile image) {
        Profile profile = profileRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        if (!user.getId().equals(userId)) {
            throw new InvalidUserException();
        }
    }

    public static ProfileResponseDto toResponseDto(Profile profile) {
        return ProfileResponseDto.builder()
                .nickname(profile.getNickname())
                .introduction(profile.getIntroduction())
                .build();
    }
}
