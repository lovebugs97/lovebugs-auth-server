package com.lovebugs.auth.dto;

public record TokenResponse(
        String grantType,
        String accessToken,
        String refreshToken
) {

}
