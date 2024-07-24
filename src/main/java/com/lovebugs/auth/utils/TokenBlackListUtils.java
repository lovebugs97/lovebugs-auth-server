package com.lovebugs.auth.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenBlackListUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void addToBlackList(String token, Date expirationDate) {
        String key = BLACKLIST_PREFIX + token;
        long expTimeInMillis = expirationDate.getTime() - System.currentTimeMillis();
        redisTemplate.opsForValue().set(key, true, expTimeInMillis, TimeUnit.MILLISECONDS);
        log.info("Token Added to BlackList: {}, Expiration Date: {}", key, expTimeInMillis);
    }

    public boolean isTokenBlackListed(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}