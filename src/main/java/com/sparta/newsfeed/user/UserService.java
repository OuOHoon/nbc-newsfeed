package com.sparta.newsfeed.user;

import com.sparta.newsfeed.common.exception.user.ExistingUserException;
import com.sparta.newsfeed.common.exception.user.NotFoundUserException;
import com.sparta.newsfeed.common.exception.user.SamePasswordException;
import com.sparta.newsfeed.common.exception.user.WrongPasswordException;
import com.sparta.newsfeed.profile.ProfileService;
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
    private final ProfileService profileService;
    
    @Value("{$spring.mail.username}")
    private static String senderEmail;

    //회원가입
    public void signup(SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 이미 가입한 email 이라면 예외 발생
        if(userRepository.findByUsername(username).isPresent()) {
            throw new ExistingUserException();
        }

        User user = new User(username, password, UserRoleEnum.GUEST);
        userRepository.save(user);
    }

    //메일 인증 성공 후 userRole 변경
    @Transactional
    public void mailAuth(MailAuthRequestDto requestDto, UserDetailsImpl userDetails) {

        AuthCode authCode = authCodeRepository.findByUsername(userDetails.getUsername()).orElseThrow(NotFoundUserException::new);

        //인증번호 만료시간 검사 - 10분
        Duration duration = Duration.between(authCode.getCreatedAt(), LocalDateTime.now());

        //10분이 경과했다면 엔티티 삭제하고 다시 인증하라는 메시지 보내기
        if(duration.getSeconds() > 600) {
            authCodeRepository.delete(authCode);
            throw new ExpiredAuthCodeException();
        }

        //인증번호 일치 검사
        if(requestDto.getAuthcode() != authCode.getCode()) throw new WrongAuthCodeException();

        //모두 통과 시 Role 변경
        User user = userRepository.findByUsername(userDetails.getUsername()).orElseThrow(NotFoundUserException::new);
        user.setRole(UserRoleEnum.USER);
        // 프로필도 함께 생성
        profileService.createProfile(user.getId());
    }

    //로그인
    public String login(LoginRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // username으로 찾을 수 없다면 예외 발생
        User user = userRepository.findByUsername(username).orElseThrow(NotFoundUserException::new);

        //비밀번호 일치여부 검사
        if(!passwordEncoder.matches(password, user.getPassword())) {
            throw new WrongPasswordException();
        }

        //Role이 GUEST일 시 이메일 인증 과정 진행
        if(user.getRole().equals(UserRoleEnum.GUEST)) {

            //인증 번호 생성 후 메일 보내기
            int authCode = makeRandomCode();
            sendAuthCode(username, authCode);
            authCodeRepository.save(new AuthCode(username, authCode));
            return "가입하신 이메일로 인증코드가 발송되었습니다.";
        }

        //이메일 인증 과정이 이미 끝나서 User Role이 USER일 시 그냥 로그인
        return "로그인이 완료되었습니다.";
    }

    //비밀번호 변경
    @Transactional
    public void changePassword(ChangePasswordRequestDto requestDto, UserDetailsImpl userDetails) {

        // 유저를 찾을 수 없다면 예외 발생
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
