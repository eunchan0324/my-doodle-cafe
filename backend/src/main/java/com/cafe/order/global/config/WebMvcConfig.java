package com.cafe.order.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 설정 파일 등록
public class WebMvcConfig implements WebMvcConfigurer { // MVC 설정 커스터마이징

    @Override
    public void addCorsMappings(CorsRegistry registry) { // CORS 규칙 정의
        registry.addMapping("/**") // 1. 범위 설정 - 모든 경로에 대해
                .allowedOrigins("http://localhost:5173") // 2. 허용할 출처 - React(Vite) 포트 허용
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS") // 3. 허용할 행동 - 모든 HTTP 메서드 허용
                .allowedHeaders("*") // 4. 허용할 헤더 - 모든 헤더 허용
                .allowCredentials(true); // 5. 인증 정보 허용 - 쿠키,세션 인증 허용
    }
}
