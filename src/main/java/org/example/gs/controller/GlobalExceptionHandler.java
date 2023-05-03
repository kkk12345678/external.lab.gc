package org.example.gs.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String EOL = System.lineSeparator();

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, "Illegal path variable 'id'. Must be an integer." + EOL +
                ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({NoSuchElementException.class, IllegalArgumentException.class})
    protected ResponseEntity<Object> handleBadRequestException(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(ResponseStatusException.class)
    protected ResponseEntity<Object> handleIllegalArgumentException(
            ErrorResponseException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getBody().getDetail(), ex.getHeaders(), ex.getStatusCode(), request);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleOtherException(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex,
                "Service is currently unavailable." + EOL + ex.getMessage(),
                new HttpHeaders(), HttpStatus.SERVICE_UNAVAILABLE, request);
    }
}