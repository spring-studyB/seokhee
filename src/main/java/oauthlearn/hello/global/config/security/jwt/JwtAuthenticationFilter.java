package oauthlearn.hello.global.config.security.jwt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        // token : "Bearer "을 제외한 [header.payload.signature] 값
        String token = resolveToken(request);
        log.info("Jwt Authentication Check Filter token : {}", token);

        // 토큰을 검증하고 SecurityContextHolder에 등록
        if (StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
            Authentication authentication = jwtProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터 실행
        chain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        // 헤더에서 Authorization 정보 추출
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            // "Bearer "을 제외한 값 리턴
            return bearerToken.substring(7);
        }

        return null;
    }
}
