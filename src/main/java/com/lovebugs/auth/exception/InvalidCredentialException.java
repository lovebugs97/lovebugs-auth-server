package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class InvalidCredentialException extends RuntimeException {
    private final ErrorCode errorCode;

    public InvalidCredentialException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
