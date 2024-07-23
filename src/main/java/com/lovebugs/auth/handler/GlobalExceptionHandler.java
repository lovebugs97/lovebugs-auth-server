package com.lovebugs.auth.handler;


import com.lovebugs.auth.dto.ErrorResponse;
import com.lovebugs.auth.exception.AuthenticationFailureException;
import com.lovebugs.auth.exception.EmailDuplicationException;
import com.lovebugs.auth.exception.ErrorCode;
import com.lovebugs.auth.exception.MemberNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationFailureException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationFailedException(AuthenticationFailureException e) {
        log.error(e.getMessage());
        return handleExceptionInternal(e.getErrorCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EmailDuplicationException.class)
    protected ResponseEntity<ErrorResponse> handleEmailDuplicationException(EmailDuplicationException e) {
        log.error(e.getMessage());
        return handleExceptionInternal(e.getErrorCode());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MemberNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException e) {
        log.error(e.getMessage());
        return handleExceptionInternal(e.getErrorCode());
    }

    private ResponseEntity<ErrorResponse> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity
                .status(errorCode.getHttpStatus().value())
                .body(new ErrorResponse(errorCode));
    }
}
