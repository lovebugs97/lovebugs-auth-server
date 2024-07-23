package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends RuntimeException {
    private final ErrorCode errorCode;

    public MemberNotFoundException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
