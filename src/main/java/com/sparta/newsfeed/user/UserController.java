package com.sparta.newsfeed.user;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.common.exception.NotFoundUserException;
import com.sparta.newsfeed.security.JwtUtil;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.user.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto requestDto, HttpServletResponse response) {
        userService.signup(requestDto);
        //인증번호 보낼 때 유저 정보 가져올 수 있게 게스트용 쿠키 전달
        response.setHeader("Authorization", jwtUtil.createToken(requestDto.getUsername(),true));
        return ResponseEntity.ok().body(BaseResponse.of("가입하신 이메일로 인증번호가 전송되었습니다.", HttpStatus.CREATED.value(), new SignupResponseDto(requestDto.getUsername())));
    }

    //메일 인증
    @PostMapping("/mail-auth")
    public ResponseEntity<BaseResponse<Void>> mailAuth(@RequestBody MailAuthRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.mailAuth(requestDto, userDetails);
        return ResponseEntity.ok().body(BaseResponse.of("인증 처리가 완료되었습니다.", HttpStatus.OK.value(), null));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Void>> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        userService.login(requestDto);
        //로그인 가능 시 토큰 생성 후 헤더에 넣기
        response.setHeader("Authorization", jwtUtil.createToken(requestDto.getUsername(),true));
        return ResponseEntity.ok().body(BaseResponse.of("로그인 성공", HttpStatus.OK.value(), null));
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(HttpServletResponse response) {
        //만료기한이 0 인 토큰 발급
        response.setHeader("Authorization", jwtUtil.createToken(null, false));
        return ResponseEntity.ok().body(BaseResponse.of("로그아웃 성공", HttpStatus.OK.value(), null));
    }

    @PutMapping("/password")
    public ResponseEntity<BaseResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.changePassword(requestDto, userDetails);
        return ResponseEntity.ok().body(BaseResponse.of("패스워드 변경 성공!", HttpStatus.OK.value(), null));
    }
}