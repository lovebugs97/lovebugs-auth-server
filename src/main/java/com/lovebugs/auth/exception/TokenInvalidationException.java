package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class TokenInvalidationException extends BaseRuntimeException {
    public TokenInvalidationException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
