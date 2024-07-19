package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class EmailDuplicatedException extends RuntimeException {
    private final ErrorCode errorCode;

    public EmailDuplicatedException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

