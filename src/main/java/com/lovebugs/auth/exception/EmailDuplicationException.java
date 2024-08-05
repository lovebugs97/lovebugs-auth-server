package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class EmailDuplicationException extends BaseRuntimeException {
    public EmailDuplicationException(final ErrorCode errorCode) {
        super(errorCode);
    }
}

