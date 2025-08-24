package com.example.project.exceptions;

import java.util.List;

public class NotFoundException extends RuntimeException {
    private final List<String> errors;

    public NotFoundException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public NotFoundException(String message) {
        super(message);
        this.errors = List.of(message);
    }
}
