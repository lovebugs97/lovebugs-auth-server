package com.lovebugs.auth.dto;

import com.lovebugs.auth.exception.ErrorCode;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class ErrorResponse {
    private final LocalDateTime timeStamp;
    private final Integer statusCode;
    private final String error;
    private final String message;

    public ErrorResponse(ErrorCode errorCode) {
        this.timeStamp = LocalDateTime.now();
        this.statusCode = errorCode.getHttpStatus().value();
        this.error = errorCode.getHttpStatus().name();
        this.message = errorCode.getMessage();
    }
}
