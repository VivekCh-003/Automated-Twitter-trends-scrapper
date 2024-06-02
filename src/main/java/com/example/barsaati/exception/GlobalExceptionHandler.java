package com.example.barsaati.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(LoginException.class)
    public String handleInvalidQuantityException(LoginException ex) {
        return ex.getMessage();
    }
}
