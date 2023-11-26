package com.sparta.newsfeed.user;

import com.sparta.newsfeed.common.exception.user.ExistingUserException;
import com.sparta.newsfeed.common.exception.user.NotFoundUserException;
import com.sparta.newsfeed.common.exception.user.SamePasswordException;
import com.sparta.newsfeed.common.exception.user.WrongPasswordException;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.user.dto.ChangePasswordRequestDto;
import com.sparta.newsfeed.user.dto.LoginRequestDto;
import com.sparta.newsfeed.user.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    //회원가입
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        if(userRepository.findByUsername(username).isPresent()) {
            throw new ExistingUserException();
        }

        User user = new User(username, password);
        userRepository.save(user);
    }

    //로그인
    public void login(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        User user = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);

        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongPasswordException();
        }
    }

    //비밀번호 변경
    @Transactional
    public void changePassword(ChangePasswordRequestDto requestDto, UserDetailsImpl userDetails) {

        User user = userRepository.findById(userDetails.getUser().getId())
                .orElseThrow(NotFoundUserException::new);
        String newPassword = passwordEncoder.encode(requestDto.getNewpassword());

        //비밀번호 재입력 오류
        if(!passwordEncoder.matches(requestDto.getExistingpassword(), user.getPassword())) {
            throw new WrongPasswordException();
        }

        //기존 비밀번호와 새로운 비밀번호가 같은 경우
        if(requestDto.getExistingpassword().equals(requestDto.getNewpassword())) {
            throw new SamePasswordException();
        }

        user.setPassword(newPassword);
    }
}
