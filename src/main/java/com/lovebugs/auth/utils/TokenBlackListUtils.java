package com.lovebugs.auth.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lovebugs.auth.dto.admin.AdminDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenBlackListUtils {
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtUtils jwtUtils;
    private final ObjectMapper objectMapper;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public void addToBlackList(String token, Date expirationDate) {
        String key = BLACKLIST_PREFIX + token;
        long expTimeInMillis = expirationDate.getTime() - System.currentTimeMillis();

        // Service Layer에서 만료 판단을 한 뒤에 넘어오긴 하지만, 방어 로직 삽입
        if (expTimeInMillis <= 0) {
            return;
        }

        AdminDto.AddBlackListObject obj = new AdminDto.AddBlackListObject(new Date(), expirationDate, token);
        redisTemplate.opsForValue().set(key, obj, expTimeInMillis, TimeUnit.MILLISECONDS);
        log.info("Token Added to BlackList: {}, Expiration Date: {}s", key, expTimeInMillis / 1000);
    }

    public List<AdminDto.BlackListTokenInfo> getBlackListedTokens() {
        List<AdminDto.BlackListTokenInfo> tokens = new ArrayList<>();

        try (Cursor<String> scans = redisTemplate.scan(ScanOptions.scanOptions().match(BLACKLIST_PREFIX + "*").build())) {
            scans.stream().forEach(key -> {
                String token = key.replace(BLACKLIST_PREFIX, "");
                Object value = redisTemplate.opsForValue().get(key);

                if (value != null) {
                    try {
                        AdminDto.AddBlackListObject obj = objectMapper.convertValue(value, AdminDto.AddBlackListObject.class);
                        String email = jwtUtils.extractEmail(obj.getToken());
                        String role = jwtUtils.extractRole(obj.getToken());

                        AdminDto.BlackListTokenInfo tokenInfo =
                                new AdminDto.BlackListTokenInfo(token, email, role, obj.getAddedAt(), obj.getExpAt());

                        tokens.add(tokenInfo);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            });
        }

        return tokens;
    }

    public boolean isTokenBlackListed(String token) {
        String key = BLACKLIST_PREFIX + token;
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}