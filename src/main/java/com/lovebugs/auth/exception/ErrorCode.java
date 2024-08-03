package com.lovebugs.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /* 토큰 관련 에러 응답만 401 유지해야 함 */
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Member not found"),
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "Email duplication"),
    AUTHENTICATION_FAIL(HttpStatus.BAD_REQUEST, "Unauthorized"),
    TOKEN_INVALIDATION(HttpStatus.FORBIDDEN, "Token Invalidation"),
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
