package org.example.gc.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(Exception e) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "path_variable_id_invalid",
                        e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    protected ResponseEntity<Object> handleNoSuchElementException(Exception e) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "entity_not_found",
                        e.getMessage()),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoSuchUserException.class)
    protected ResponseEntity<Object> handleNoSuchUserException(Exception e) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        HttpStatus.NOT_FOUND.value(),
                        "user_not_found",
                        e.getMessage()),
                HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AlreadyExistsException.class)
    protected ResponseEntity<Object> handleAlreadyExistsException(Exception e) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "entity_already_exists",
                        e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(Exception e) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        "dto_constrains_violation",
                        e.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotAuthorizedUserException.class)
    protected ResponseEntity<Object> handleNotAuthorizedUserException(Exception e) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        HttpStatus.UNAUTHORIZED.value(),
                        "user_not_authorized",
                        e.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    /*

    @ExceptionHandler(IllegalStateException.class)
    protected ResponseEntity<Object> handleIllegalStateException(
            Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }


     */

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleOtherException(Exception e) {
        return new ResponseEntity<>(
                new ExceptionResponse(
                        HttpStatus.SERVICE_UNAVAILABLE.value(),
                        "service_unavailable",
                        e.getMessage()),
                HttpStatus.SERVICE_UNAVAILABLE);
    }
}