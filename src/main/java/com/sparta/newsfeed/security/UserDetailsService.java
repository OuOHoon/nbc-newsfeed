package com.sparta.newsfeed.security;

import com.sparta.newsfeed.user.User;
import com.sparta.newsfeed.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService {

    private final UserRepository userRepository;
    public UserDetailsImpl getUserDetails(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));
        return new UserDetailsImpl(user);
    }
}
