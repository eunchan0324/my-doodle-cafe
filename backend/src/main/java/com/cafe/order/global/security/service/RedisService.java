package com.cafe.order.global.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Redis를 더 편리하게 사용하기 위한 도우미 클래스 (Service)
 */
@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 데이터를 저장합니다.
     * @param key 저장할 이름 (예: JWT 토큰)
     * @param data 저장할 내용 (예: "logout")
     * @param timeout 유효 시간 (밀리초 단위) - 이 시간이 지나면 Redis에서 자동으로 삭제됩니다!
     */
    public void setValues(String key, String data, long timeout) {
        redisTemplate.opsForValue().set(key, data, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 데이터를 가져옵니다.
     */
    public Object getValues(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 데이터를 삭제합니다.
     */
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 데이터가 존재하는지 확인합니다.
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
