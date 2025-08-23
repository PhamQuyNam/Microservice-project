package com.npq.api_gateway.exception;

import com.npq.api_gateway.dto.AuthenticationResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class HandleException extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Object> hadleException(CustomException ex) {
        AuthenticationResponseError authenticationResponseErr =
                new AuthenticationResponseError(HttpStatus.UNAUTHORIZED.value(), ex.getMessage());
        return new ResponseEntity<>(authenticationResponseErr, HttpStatus.UNAUTHORIZED);
    }
}
