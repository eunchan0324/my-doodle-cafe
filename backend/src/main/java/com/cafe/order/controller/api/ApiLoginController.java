package com.cafe.order.controller.api;

import com.cafe.order.domain.user.entity.User;
import com.cafe.order.domain.user.service.UserService;
import com.cafe.order.global.security.dto.LoginRequest;
import com.cafe.order.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
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
public class ApiLoginController {

    private final UserService userService; // 사용자 조회용
    private final PasswordEncoder passwordEncoder; // 비밀번호 비교용
    private final JwtTokenProvider jwtTokenProvider; // 토큰 발급용

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
    public ResponseEntity<?> logout() {
        // 1. 현재 쓰레드의 보안 컨텐스트를 비운다.
        SecurityContextHolder.clearContext();

        // 2. 응답 구성
        Map<String, String> response = new HashMap<>();
        response.put("message", "로그아웃 되었습니다.");

        return ResponseEntity.ok(response);
    }
}
