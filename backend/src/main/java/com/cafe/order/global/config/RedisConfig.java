package com.cafe.order.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration // "이 클래스는 설정 파일이야!"라고 스프링에게 알려줌
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    /**
     * Redis 연결을 위한 '공장' 만들기
     * - properties에 적은 주소와 포트를 사용해 실제 연결 통로를 생성합니다.
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    /**
     * 실제 데이터를 주고받을 '리모컨(RedisTemplate)' 만들기
     * - 우리가 자바 코드에서 "저장해!", "가져와!"라고 명령할 때 쓰는 도구입니다.
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        
        // 위에서 만든 '공장(연결 통로)'을 연결함
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // [중요] 통역사 설정: 데이터를 깔끔한 문자열로 저장하기 위함
        // Key(이름)도 문자열, Value(값)도 문자열로 저장하겠다는 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        
        return redisTemplate;
    }
}
