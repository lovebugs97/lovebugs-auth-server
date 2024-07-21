package com.lovebugs.auth.handler;


import com.lovebugs.auth.dto.ErrorResponse;
import com.lovebugs.auth.exception.AuthenticationFailureException;
import com.lovebugs.auth.exception.EmailDuplicationException;
import com.lovebugs.auth.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AuthenticationFailureException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationFailedException(AuthenticationFailureException e) {
        log.error(e.getMessage());
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(EmailDuplicationException.class)
    protected ResponseEntity<ErrorResponse> handleEmailDuplicationException(EmailDuplicationException e) {
        log.error(e.getMessage());
        return handleExceptionInternal(e.getErrorCode());
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new ErrorResponse(errorCode));
    }
}
