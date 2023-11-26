package com.sparta.newsfeed.profile;

import com.sparta.newsfeed.common.S3Uploader;
import com.sparta.newsfeed.common.exception.profile.NotFoundProfileException;
import com.sparta.newsfeed.common.exception.profile.SameNicknameException;
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
import java.util.Optional;

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
        User user = userRepository.findById(userId).orElseThrow(NotFoundProfileException::new);
        Profile profile = Profile.builder()
                .nickname(RandomStringUtils.random(10, true, true)) // 중복 가능성 매우 낮으므로 무시
                .introduction("")
                .build();
        profile.setUser(user);
        profileRepository.save(profile);
        return toResponseDto(profile);
    }

    public ProfileResponseDto createProfile(Long userId, ProfileRequestDto request) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundProfileException::new);
        Optional<Profile> optionalProfile = profileRepository.findByNickname(request.getNickname());
        // 닉네임 중복 체크
        if (optionalProfile.isPresent()) {
            throw new SameNicknameException();
        }
        Profile profile = Profile.builder()
                .nickname(request.getNickname())
                .introduction(request.getIntroduction())
                .build();
        profile.setUser(user);
        profileRepository.save(profile);
        return toResponseDto(profile);
    }

    @Transactional
    public ProfileResponseDto updateProfile(Long userId, User user, ProfileRequestDto request) {
        Profile profile = profileRepository.findByUserId(userId).orElseThrow(NotFoundProfileException::new);
        Optional<Profile> optionalProfile = profileRepository.findByNickname(request.getNickname());

        // 수정 권한 체크
        if (!profile.getUser().getId().equals(user.getId())) {
            throw new InvalidUserException();
        }
        // 닉네임 중복 체크
        if (optionalProfile.isPresent()) {
            throw new SameNicknameException();
        }

        profile.update(request.getNickname(), request.getIntroduction());
        return toResponseDto(profile);
    }

    @Transactional
    public String uploadProfileImage(Long userId, User user, MultipartFile image) throws IOException {
        Profile profile = profileRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new);

        // 수정 권한 체크
        if (!user.getId().equals(userId)) {
            throw new InvalidUserException();
        }

        String uploadUrl = s3Uploader.uploadProfileImage(userId.toString(), image);
        profile.setImageUrl(uploadUrl);
        return uploadUrl;
    }

    public String getProfileImage(Long userId, User user) {
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
