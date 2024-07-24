package com.lovebugs.auth.dto.token;

import com.lovebugs.auth.dto.auth.TokenDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public class TokenReIssueDto {
    @Getter
    @RequiredArgsConstructor
    public static class Request {
        private final Integer id;
        private final String email;
        private final String refreshToken;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Response {
        private final String grantType;
        private final String accessToken;
        private final String refreshToken;

        public static Response of(TokenDto tokenDto) {
            return new Response(tokenDto.getGrantType(), tokenDto.getAccessToken(), tokenDto.getRefreshToken());
        }
    }
}
