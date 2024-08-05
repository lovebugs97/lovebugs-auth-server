package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class BaseRuntimeException extends RuntimeException {
    private final ErrorCode errorCode;

    public BaseRuntimeException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
