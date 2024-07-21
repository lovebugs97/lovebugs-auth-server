package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class EmailDuplicationException extends RuntimeException {
    private final ErrorCode errorCode;

    public EmailDuplicationException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}

