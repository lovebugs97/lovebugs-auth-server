package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class AuthenticationFailureException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthenticationFailureException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
