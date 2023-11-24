package com.sparta.newsfeed.user;

import com.sparta.newsfeed.common.exception.NotFoundUserException;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.user.dto.ChangePasswordRequestDto;
import com.sparta.newsfeed.user.dto.LoginRequestDto;
import com.sparta.newsfeed.user.dto.MailAuthRequestDto;
import com.sparta.newsfeed.user.dto.SignupRequestDto;
import com.sparta.newsfeed.user.exception.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final AuthCodeRepository authCodeRepository;
    
    @Value("{$spring.mail.username}")
    private static String senderEmail;

    //회원가입
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        if(userRepository.findByUsername(username).isPresent()) {
            throw new ExistingUserException();
        }

        User user = new User(username, password, UserRoleEnum.GUEST);
        userRepository.save(user);

        //인증 번호 생성 후 메일 보내기
        int authCode = makeRandomCode();
        sendAuthCode(username, authCode);
        authCodeRepository.save(new AuthCode(username, authCode));
    }

    //메일 인증 성공 후 userRole 변경
    @Transactional
    public void mailAuth(MailAuthRequestDto requestDto, UserDetailsImpl userDetails) {

        AuthCode authCode = authCodeRepository.findByUsername(userDetails.getUsername()).orElseThrow(NotFoundUserException::new);

        //인증번호 만료시간 검사 - 1시간
        Duration duration = Duration.between(authCode.getCreatedAt(), LocalDateTime.now());
        if(duration.getSeconds() > 3600) throw new ExpiredAuthCodeException();

        //인증번호 일치 검사
        if(requestDto.getAuthcode() != authCode.getCode()) throw new WrongAuthCodeException();

        //모두 통과 시 Role 변경
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NotFoundUserException::new);
        user.setRole(UserRoleEnum.USER);
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

    //6자리 랜덤 코드 만들기
    public int makeRandomCode() {
        java.util.Random generator = new Random(System.currentTimeMillis());
        return generator.nextInt(1000000) % 1000000;
    }

    public void sendAuthCode(String username, int code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(username);
        message.setSubject("nbc-newsfeed 가입을 위한 인증번호입니다.");
        message.setText("nbc-newsfeed 가입을 위한 인증번호입니다. \n\n" + code);

        mailSender.send(message);
    }
}
