package com.lovebugs.auth.utils;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;


@Component
public class CookieUtils {
    public ResponseCookie generateCookie(String key, String value, Long expSec) {
        return ResponseCookie.from(key, value)
                .httpOnly(true)
                .secure(true)
                .domain("localhost")
                .sameSite("None")
                .path("/api")
                .maxAge(expSec)
                .build();
    }
}
