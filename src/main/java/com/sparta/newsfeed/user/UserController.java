package com.sparta.newsfeed.user;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.security.JwtUtil;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.user.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<BaseResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        // 회원가입 로직 -> user table에 저장하고 Role은 GUEST로 둠.
        userService.signup(requestDto);

        return ResponseEntity.ok().body(BaseResponse.of("회원가입이 완료되었습니다. 최초 로그인 시 이메일 인증을 진행해주세요.",
                true, new SignupResponseDto(requestDto.getUsername())));
    }

    //메일 인증
    @PostMapping("/mail-auth")
    public ResponseEntity<BaseResponse<Void>> mailAuth(@RequestBody MailAuthRequestDto requestDto,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.mailAuth(requestDto, userDetails);
        return ResponseEntity.ok().body(BaseResponse.of("인증 처리가 완료되었습니다.", true, null));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Void>> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        String message = userService.login(requestDto);

        //토큰 생성 후 헤더에 넣기
        response.setHeader("Authorization", jwtUtil.createToken(requestDto.getUsername(),true));
        return ResponseEntity.ok().body(BaseResponse.of(message, true, null));
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(HttpServletResponse response) {
        //만료기한이 0 인 토큰 발급
        response.setHeader("Authorization", jwtUtil.createToken(null, false));
        return ResponseEntity.ok().body(BaseResponse.of("로그아웃 성공", true, null));
    }

    @PutMapping("/password")
    public ResponseEntity<BaseResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.changePassword(requestDto, userDetails);
        return ResponseEntity.ok().body(BaseResponse.of("패스워드 변경 성공!", true, null));
    }
}