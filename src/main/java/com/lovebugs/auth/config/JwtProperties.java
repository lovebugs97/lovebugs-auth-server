package com.lovebugs.auth.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("jwt")
public class JwtProperties {
    private final String secretKey;
    private final String prefix;
    private final Long accessTokenExpiration;
    private final Long refreshTokenExpiration;
}
