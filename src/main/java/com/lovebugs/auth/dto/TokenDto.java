package com.lovebugs.auth.dto;

public record TokenDto(
        String grantType,
        String accessToken,
        String refreshToken
) {

}
