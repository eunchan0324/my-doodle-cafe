package com.cafe.order.global.security.jwt;

import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.service.UserService;
import com.cafe.order.global.security.dto.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * 모든 요청(Request)마다 토큰을 검사하는 필터
 * OncePerRequestFilter를 상속받으면, 하나의 요청당 딱 한 번만 실행됨을 보장
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService; 
    // Redis 도우미 추가!
    private final com.cafe.order.global.security.service.RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws SecurityException, IOException, ServletException {

        // 1. 요청 헤더에서 토큰 꺼내기
        String token = resolveToken(request);

        // 2. 토큰이 있고 유효한지 1차 검사
        if (token != null && jwtTokenProvider.validateToken(token)) {
            
            // [추가] 2차 검사: Redis 블랙리스트에 있는지 확인
            if (redisService.hasKey(token)) {
                log.info("[JWT 인증 실패] 로그아웃된 토큰입니다.");
                // 블랙리스트에 있다면 다음 단계로 가지 않고 바로 종료
                filterChain.doFilter(request, response);
                return;
            }

            // 3. 토큰에서 아이디 꺼내기
            String loginId = jwtTokenProvider.getLoginId(token);

            log.info("[JWT 인증 성공] 사용자: {}", loginId);

            // 4. DB에서 사용자 정보를 가져와서 '인증 객체(Authentication)' 를 만든다.
            // (주의 : 매번 DB를 조회하는 것은 성능상 아쉬울 수 있으나, 가장 확실한 방법)
            UserDetails userDetails = loadUserByLoginId(loginId);

            if (userDetails != null) {
                // 5. 스프링 시큐리티에게 "이 사람 로그인 됐어!" 라고 알려줌 (SecurityContext에 저장)
                var authentication = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 6. 다음 필터로 넘김 (이걸 안하면 요청이 여기서 멈춤)
        filterChain.doFilter(request, response);
    }

    /**
     * Request Header에서 토큰 정보를 꺼내오는 메서드
     * 보통 "Authorization: Bearer <토큰>" 형태로 들어온다.
     */
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        // "Bearer " 로 시작하는지 확인하고, 뒤에 있는 진짜 토큰만 잘라서 리턴
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // 앞의 "Bearer " 7글자를 자름
        }
        return null;
    }

    /**
     * LoginId로 DB에서 사용자 정보를 찾아 UserDetails로 변환하는 메서드
     */
    private UserDetails loadUserByLoginId(String loginId) {
        User user = userService.findByLoginId(loginId).orElse(null);
        if (user == null) {
            return null;
        }
        return new CustomUserDetails(user);
    }
}
