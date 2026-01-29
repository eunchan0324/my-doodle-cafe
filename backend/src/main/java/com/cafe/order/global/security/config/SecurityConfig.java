package com.cafe.order.global.security.config;

import com.cafe.order.domain.user.service.UserService;
import com.cafe.order.global.security.handler.CustomAuthenticationSuccessHandler;
import com.cafe.order.global.security.jwt.JwtAuthenticationEntryPoint;
import com.cafe.order.global.security.jwt.JwtAuthenticationFilter;
import com.cafe.order.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;

    // JWT 관련 의존성 추가
    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))

                // 인증 실패 시 처리 (HTML 로그인 페이지로 리다이렉트 방지)
                .exceptionHandling(handler -> handler
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint))

                // 1. JWT 필터 등록
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider, userService),
                        UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(auth -> auth
                        // 완전 허용 (WhiteList) - H2 콘솔 및 정적 리소스 허용
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/css/**", "/js/**", "/images/**", "/favicon.ico", "/error").permitAll()
                        .requestMatchers("/", "/login", "/users/signup", "/join", "/login-proc").permitAll()
                        .requestMatchers("/api/v1/users/**").permitAll() // 회원가입
                        .requestMatchers("/api/v1/auth/**", "/api/health").permitAll() // JWT 로그인, 프론트 테스트용
                        
                        // 레거시(Thymeleaf) 페이지 권한
                        .requestMatchers("/customer/**").hasRole("CUSTOMER")
                        .requestMatchers("/seller/**").hasRole("SELLER")
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 로그인 시 통과
                        .anyRequest().authenticated()
                )
                // 기존 폼 로그인 유지 (타임리프용)
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login-proc")
                        .usernameParameter("loginId")
                        .successHandler(customAuthenticationSuccessHandler)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }

    // CORS 설정 구체화
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 프론트엔드 주소 허용 (포트 5173)
        configuration.setAllowedOrigins(List.of("http://localhost:5173", "http://218.156.123.200:5173"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // 모든 헤더 허용
        configuration.setAllowedHeaders(List.of("*"));

        // 쿠키/세션 인증 정보 허용 (로그인 유지에 필수)
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 적용
        return source;
    }
}