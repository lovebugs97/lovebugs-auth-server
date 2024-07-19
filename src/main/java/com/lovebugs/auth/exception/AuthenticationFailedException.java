package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class AuthenticationFailedException extends RuntimeException {
    private final ErrorCode errorCode;

    public AuthenticationFailedException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
