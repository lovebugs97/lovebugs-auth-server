package com.lovebugs.auth.exception;

import lombok.Getter;

@Getter
public class MemberNotFoundException extends BaseRuntimeException {
    public MemberNotFoundException(final ErrorCode errorCode) {
        super(errorCode);
    }
}
