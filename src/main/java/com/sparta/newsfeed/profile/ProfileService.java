package com.sparta.newsfeed.profile;

import com.sparta.newsfeed.profile.dto.ProfileRequestDto;
import com.sparta.newsfeed.profile.dto.ProfileResponseDto;
import com.sparta.newsfeed.user.InvalidUserException;
import com.sparta.newsfeed.user.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileResponseDto findOne(Long userId) {
        return toResponseDto(profileRepository.findByUserId(userId).orElseThrow(NotFoundUserException::new));
    }

    @Transactional
    public ProfileResponseDto update(Long userId, String username, ProfileRequestDto request) {
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
