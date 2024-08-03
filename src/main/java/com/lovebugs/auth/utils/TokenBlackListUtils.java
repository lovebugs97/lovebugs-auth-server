package com.lovebugs.auth.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
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
        log.info("Token Added to BlackList: {}, Expiration Date: {}s", key, expTimeInMillis / 1000);
    }

    public void getBlackListedTokens() {
        try (Cursor<String> scans = redisTemplate.scan(ScanOptions.scanOptions().match(BLACKLIST_PREFIX + "*").build())) {
            scans.stream().forEach(key -> {
                Object value = redisTemplate.opsForValue().get(key);
                log.info("{}: {}", key, value);
            });
        }
    }

    public boolean isTokenBlackListed(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}