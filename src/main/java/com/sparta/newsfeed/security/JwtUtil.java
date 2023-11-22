package com.sparta.newsfeed.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 240 * 60 * 1000L;

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //Header에서 "Bearer " 를 제외한 토큰 값 추출
    public String getJwtFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        //bearerToken이 빈 값이 아니고 우리가 지정해준 토큰 식별자를 가지고 있다면 식별자 띄고 값 가져온다.
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //유효한 토큰인지 검증
    public boolean validateToken(String tokenValue) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tokenValue);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    //토큰에서 claim 추출
    public Claims getUserInfo(String tokenValue) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tokenValue).getBody();
    }


    //토큰 생성
    //forLogin가 false라면 로그아웃을 위한 토큰으로 만료시간이 0인 토큰을 제작
    public String createToken(String username, boolean forExpire ) {
        Date date = new Date();
        Long tokenTime = forExpire ? TOKEN_TIME : 0;

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) //만료시간 설정
                        .setIssuedAt(date) //발급일
                        .signWith(key, signatureAlgorithm)//암호화
                        .compact();
    }
}
