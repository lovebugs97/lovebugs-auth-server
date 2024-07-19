package com.lovebugs.auth.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
public class CookieProvider {
    public ResponseCookie generateCookie(String key, String value) {
        log.info("Generate cookie for key: {} and value: {}", key, value);

        return ResponseCookie.from(key, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(Duration.ofDays(7))
                .build();
    }
}
