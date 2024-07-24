package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class TokenInvalidationException extends RuntimeException {
    private final ErrorCode errorCode;

    public TokenInvalidationException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
