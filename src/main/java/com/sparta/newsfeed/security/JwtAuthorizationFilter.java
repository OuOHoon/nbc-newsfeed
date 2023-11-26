package com.sparta.newsfeed.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.newsfeed.common.BaseResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //헤더에서 토큰값 substring해서 가져오기
        String tokenValue = jwtUtil.getJwtFromHeader(request);

        //토큰이 Null이나 ""가 아니라면
        if(StringUtils.hasText(tokenValue)) {
            //유효한 토큰이 아니라면 오류메시지 전달
            if(!jwtUtil.validateToken(tokenValue)) {

                //해당 URI이거나 HTTP Method가 Get이면 유효하지 토큰이라도 요청을 거부하지 않음
                ArrayList<String> URIList = new ArrayList<>(List.of("/api/users/login", "/api/users/signup"));
                String URI = request.getRequestURI();
                if( URIList.contains(URI) || (request.getMethod().equals("GET") && URI.startsWith("/api/posts"))) {
                    filterChain.doFilter(request, response);
                    return;
                }

                //오류 메시지 반환
                ObjectMapper objectMapper = new ObjectMapper();
                BaseResponse<String> baseResponse = new BaseResponse<>("유효한 토큰이 아닙니다.", false, null);
                response.setStatus(400);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getOutputStream().write(objectMapper.writeValueAsBytes(baseResponse));
//                response.getWriter().write(objectMapper.writeValueAsString(baseResponse));
                return;
            }

            //유효한 토큰일 시
            Claims info = jwtUtil.getUserInfo(tokenValue);

            String username = info.getSubject();
            setAuthentication(username);
        }

        filterChain.doFilter(request, response);
    }

    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        UserDetailsImpl userDetails = userDetailsService.getUserDetails(username);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }
}
