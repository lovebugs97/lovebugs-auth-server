package com.lovebugs.auth.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "Member not found"),
    EMAIL_DUPLICATION(HttpStatus.BAD_REQUEST, "Email duplication"),
    AUTHENTICATION_FAIL(HttpStatus.UNAUTHORIZED, "Unauthorized"),

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
