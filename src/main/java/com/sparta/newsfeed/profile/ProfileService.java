package com.sparta.newsfeed.profile;

import com.sparta.newsfeed.common.S3Uploader;
import com.sparta.newsfeed.common.exception.user.InvalidUserException;
import com.sparta.newsfeed.common.exception.user.NotFoundUserException;
import com.sparta.newsfeed.profile.dto.ProfileRequestDto;
import com.sparta.newsfeed.profile.dto.ProfileResponseDto;
import com.sparta.newsfeed.user.User;
import com.sparta.newsfeed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
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
    public String uploadProfileImage(Long userId, User user, MultipartFile image) throws IOException {
        Profile profile = profileRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new);

        // 프로필 수정 요청한 유저랑 대상 유저랑 같은지 체크
        if (!user.getId().equals(userId)) {
            throw new InvalidUserException();
        }

        String uploadUrl = s3Uploader.uploadProfileImage(userId.toString(), image);
        profile.setImageUrl(uploadUrl);
        return uploadUrl;
    }

    public String getProfileImage(Long userId, User user) {
        // 프로필 수정 요청한 유저랑 대상 유저랑 같은지 체크.. 반복되는 유효성 검증 aop로 뺄까?
        if (!user.getId().equals(userId)) {
            throw new InvalidUserException();
        }
        Profile profile = profileRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        return profile.getImageUrl();
    }

    @Transactional
    public void deleteProfileImage(Long userId, User user) {
        if (!user.getId().equals(userId)) {
            throw new InvalidUserException();
        }
        Profile profile = profileRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new);
        String filename = userId.toString() + "." + profile.getImageUrl()
                .substring(profile.getImageUrl().lastIndexOf(".") + 1);
        log.info("filename: {}", filename);
        s3Uploader.deleteProfileImage(filename);
        profile.setImageUrl(null);
    }

    public static ProfileResponseDto toResponseDto(Profile profile) {
        return ProfileResponseDto.builder()
                .nickname(profile.getNickname())
                .introduction(profile.getIntroduction())
                .profileImageUrl(profile.getImageUrl())
                .build();
    }
}
