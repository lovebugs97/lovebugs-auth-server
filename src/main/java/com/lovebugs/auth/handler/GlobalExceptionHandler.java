package com.lovebugs.auth.handler;

import com.lovebugs.auth.dto.common.ErrorResponse;
import com.lovebugs.auth.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({
            AuthenticationFailureException.class,
            EmailDuplicationException.class,
            MemberNotFoundException.class,
            TokenInvalidationException.class,
            InvalidFilenameException.class
    })
    protected ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException e) {
        log.error(e.getMessage());

        if (e instanceof BaseRuntimeException) {
            return handleExceptionInternal(((BaseRuntimeException) e).getErrorCode());
        }

        return handleExceptionInternal(ErrorCode.UNKNOWN_ERROR);
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new ErrorResponse(errorCode));
    }
}
