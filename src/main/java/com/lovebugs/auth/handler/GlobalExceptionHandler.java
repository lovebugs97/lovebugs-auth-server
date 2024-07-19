package com.lovebugs.auth.handler;


import com.lovebugs.auth.dto.ErrorResponse;
import com.lovebugs.auth.exception.AuthenticationFailedException;
import com.lovebugs.auth.exception.EmailDuplicatedException;
import com.lovebugs.auth.exception.ErrorCode;
import com.lovebugs.auth.exception.InvalidCredentialException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(AuthenticationFailedException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationFailedException(AuthenticationFailedException e) {
        log.error(e.getMessage());
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(EmailDuplicatedException.class)
    protected ResponseEntity<ErrorResponse> handleEmailDuplicatedException(EmailDuplicatedException e) {
        log.error(e.getMessage());
        return handleExceptionInternal(e.getErrorCode());
    }

    @ExceptionHandler(InvalidCredentialException.class)
    protected ResponseEntity<ErrorResponse> handleInvalidCredentialException(InvalidCredentialException e) {
        log.error(e.getMessage());
        return handleExceptionInternal(e.getErrorCode());
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new ErrorResponse(errorCode));
    }
}
