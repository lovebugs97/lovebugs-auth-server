package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class InvalidFilenameException extends BaseRuntimeException {
    public InvalidFilenameException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
