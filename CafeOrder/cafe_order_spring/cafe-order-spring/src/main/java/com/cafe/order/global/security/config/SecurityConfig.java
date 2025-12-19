package com.cafe.order.global.security.config;

import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 1. 비밀번호 암호화 빈 등록
     * - DB에 비밀번호를 평문(1234)으로 저장하면 안됨
     * - 회원가입/로그인 시 자동으로 암호화/검증
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 2. 보안 필터 체인 설정 (관제탑 핵심 로직)
     * - URL별 접근 권한 설정
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 개발 편의성을 위해 CSRF 보호 비활성화 (나중에 켜)
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 정적 자원(css, js, images)은 누구나 접근 가능)
                        .requestMatchers("/css/**", "/images/**", "/favicon.ico").permitAll()
                        // 회원가입, 로그인 페이지는 누구나 접근 가능
                        .requestMatchers("/", "/login", "/join").permitAll()
                        // 나머지는 무조건 로그인해야 접근 가능
                        .anyRequest().authenticated()
                )

                // 폼 로그인 설정 (일단 기본 설정 유지)
                .formLogin(login -> login
//                        .loginPage("/login") // 나중에 만든 HTML을 쓸 때 주석 해제할 것
                                .permitAll()
                );

        return http.build();
    }
}
