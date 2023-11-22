package com.sparta.newsfeed.user;

import com.sparta.newsfeed.common.BaseResponse;
import com.sparta.newsfeed.security.JwtUtil;
import com.sparta.newsfeed.user.dto.LoginRequestDto;
import com.sparta.newsfeed.user.dto.SignupRequestDto;
import com.sparta.newsfeed.user.dto.SignupResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    //회원가입
    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<SignupResponseDto>> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        try {
            userService.signup(requestDto);
        } catch (IllegalArgumentException e) {
            //존재하는 아이디일 시 오류메시지 반환
            return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }
        return ResponseEntity.badRequest().body(BaseResponse.of("회원가입 성공", HttpStatus.CREATED.value(), new SignupResponseDto(requestDto.getUsername())));
    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Void>> login(@RequestBody LoginRequestDto requestDto, HttpServletResponse response) {
        try {
            userService.login(requestDto);
        } catch (IllegalArgumentException e) {
            //DB에 없는 아이디거나 비밀번호 오류 시 오류메시지 반환
            return ResponseEntity.badRequest().body(BaseResponse.of(e.getMessage(), HttpStatus.BAD_REQUEST.value(), null));
        }

        //로그인 성공 시 토큰 생성 후 헤더에 넣기
        response.setHeader("Authorization", jwtUtil.createToken(requestDto.getUsername(), true));

        return ResponseEntity.ok().body(BaseResponse.of("로그인 성공", HttpStatus.OK.value(), null));
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<BaseResponse<Void>> logout(HttpServletResponse response) {
        //만료기한이 0 인 토큰 발급
        response.setHeader("Authorization", jwtUtil.createToken(null, false));

        return ResponseEntity.ok().body(BaseResponse.of("로그아웃 성공", HttpStatus.OK.value(), null));
    }
}