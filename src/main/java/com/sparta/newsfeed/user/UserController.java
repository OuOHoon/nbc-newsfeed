package com.sparta.newsfeed.user;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.security.JwtUtil;
import com.sparta.newsfeed.security.UserDetailsImpl;
import com.sparta.newsfeed.user.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "유저 인증 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    //회원가입
    @Operation(summary = "회원 가입")
    @ApiResponse(responseCode = "201", description = "GUEST 권한으로 회원가입, 이메일 인증 후에 USER 권한 부여")
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        // 회원가입 로직 -> user table에 저장하고 Role은 GUEST로 둠.
        userService.signup(requestDto);

        return ResponseEntity.ok().body(BaseResponse.of("회원가입이 완료되었습니다. 최초 로그인 시 이메일 인증을 진행해주세요.",
                true, new SignupResponseDto(requestDto.getUsername())));
    }

    //메일 인증
    @Operation(summary = "메일 인증")
    @ApiResponse(responseCode = "200", description = "인증번호 입력 후 인증 완료 시, USER 권한 부여")
    @PostMapping("/mail-auth")
    public ResponseEntity<BaseResponse<Void>> mailAuth(@RequestBody MailAuthRequestDto requestDto,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.mailAuth(requestDto, userDetails);
        return ResponseEntity.ok().body(BaseResponse.of("인증 처리가 완료되었습니다.", true, null));
    }

    //로그인
    @Operation(summary = "로그인")
    @ApiResponse(responseCode = "200", description = "로그인 시 jwt 발급. 최초 로그인 시에는 가입한 이메일로 인증번호 발송")
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Void>> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        String message = userService.login(requestDto);

        //토큰 생성 후 헤더에 넣기
        response.setHeader("Authorization", jwtUtil.createToken(requestDto.getUsername(),true));
        return ResponseEntity.ok().body(BaseResponse.of(message, true, null));
    }

    //로그아웃
    @Operation(summary = "로그아웃")
    @ApiResponse(responseCode = "200", description = "만료된 jwt를 발급함으로써 로그아웃 처리")
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(HttpServletResponse response) {
        //만료기한이 0 인 토큰 발급
        response.setHeader("Authorization", jwtUtil.createToken(null, false));
        return ResponseEntity.ok().body(BaseResponse.of("로그아웃 성공", true, null));
    }

    @Operation(summary = "비밀번호 변경")
    @ApiResponse(responseCode = "200", description = "기존 비밀번호와 새로운 비밀번호를 입력하여 비밀번호 변경")
    @PutMapping("/password")
    public ResponseEntity<BaseResponse<Void>> changePassword(@Valid @RequestBody ChangePasswordRequestDto requestDto,
                                                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.changePassword(requestDto, userDetails);
        return ResponseEntity.ok().body(BaseResponse.of("패스워드 변경 성공!", true, null));
    }
}