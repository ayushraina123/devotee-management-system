package com.example.project.exceptions;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.Instant;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorResponse> handleHandlerMethodValidationException(HandlerMethodValidationException ex) {
        List<String> validationErrors = ex.getAllErrors()
                .stream()
                .map(MessageSourceResolvable::getDefaultMessage)
                .toList();

        ErrorResponse errors = buildErrorResponse(validationErrors);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NegativeBalanceException.class)
    public ResponseEntity<ErrorResponse> handleNegativeBalanceException(NegativeBalanceException exception) {
        List<String> validationErrors = exception.getErrors();
        ErrorResponse errorResponse = buildErrorResponse(validationErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        List<String> validationErrors = List.of(exception.getMessage());
        ErrorResponse errorResponse = buildErrorResponse(validationErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException exception) {
        List<String> validationErrors = List.of(exception.getMessage());
        ErrorResponse errorResponse = buildErrorResponse(validationErrors);
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        String validationErrors = ex.getMessage();
        ErrorResponse errors = buildErrorResponse(List.of(validationErrors));

        return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorResponse buildErrorResponse(List<String> validationErrors) {
        return ErrorResponse.builder()
                .error("Validation failed")
                .message(String.join("; ", validationErrors))
                .timestamp(Instant.now())
                .build();
    }
}
