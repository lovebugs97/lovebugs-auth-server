package com.lovebugs.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlackListService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void addToBlackList(String token, Date expirationDate) {
        long expTimeInMillis = expirationDate.getTime();
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.opsForValue().set(key, true, expTimeInMillis - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    public boolean isTokenBlackListed(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
