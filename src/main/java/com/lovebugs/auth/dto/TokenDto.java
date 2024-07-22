package com.lovebugs.auth.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TokenDto {
    private final String grantType;
    private final String accessToken;
    private final String refreshToken;
}
