package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class AuthenticationFailureException extends BaseRuntimeException {
    public AuthenticationFailureException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
