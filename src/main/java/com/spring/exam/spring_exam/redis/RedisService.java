package com.spring.exam.spring_exam.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate redisTemplate;

    public void saveInRedis(String key, String value, int expirationTime){
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, expirationTime, TimeUnit.MINUTES);
    }

    public String getDataFromRedis(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
