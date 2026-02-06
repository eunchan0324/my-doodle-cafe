package com.cafe.order.controller.api;

import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.service.UserService;
import com.cafe.order.global.security.dto.LoginRequest;
import com.cafe.order.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j // 로그 기능을 사용할 수 있게 해주는 어노테이션 추가
public class ApiLoginController {

    private final UserService userService; // 사용자 조회용
    private final PasswordEncoder passwordEncoder; // 비밀번호 비교용
    private final JwtTokenProvider jwtTokenProvider; // 토큰 발급용
    // [추가] Redis 도우미
    private final com.cafe.order.global.security.service.RedisService redisService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        // 1. 사용자 조회 (ID로 찾기)
        User user = userService.findByLoginId(request.getLoginId())
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 아이디입니다."));

        // 2. 비밀번호 검증 (입력받은 비번 vs DB에 암호화된 비번)
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 성공 > 토큰 생성
        String token = jwtTokenProvider.createToken(user.getLoginId(), user.getRole().name());

        // 4. 응답 (토큰 전달)
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", token);
        response.put("role", user.getRole().name());
        response.put("message", "로그인 성공!");

        if (user.getStore() != null) {
            response.put("storeId", String.valueOf(user.getStore().getId()));
            response.put("storeName", user.getStore().getName());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(jakarta.servlet.http.HttpServletRequest request) {
        // 1. 요청 헤더에서 토큰을 꺼내옵니다.
        String bearerToken = request.getHeader("Authorization");
        
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7); // "Bearer " 뒷부분인 실제 토큰만 추출

            // 2. 토큰의 남은 유효 시간을 계산합니다.
            Long expiration = jwtTokenProvider.getExpiration(token);

            // 3. Redis에 블랙리스트로 등록합니다.
            // 키: 토큰, 값: "logout", 유효시간: 남은 시간
            if (expiration > 0) {
                redisService.setValues(token, "logout", expiration);
                log.info("[로그아웃] 블랙리스트 등록 완료. 남은 시간: {}ms", expiration);
            }
        }

        // 4. 현재 서버 메모리에 남아있는 인증 정보도 깨끗이 지웁니다.
        SecurityContextHolder.clearContext();

        Map<String, String> response = new HashMap<>();
        response.put("message", "로그아웃 되었습니다.");

        return ResponseEntity.ok(response);
    }
}
