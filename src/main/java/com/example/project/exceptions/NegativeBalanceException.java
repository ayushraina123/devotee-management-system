package com.example.project.exceptions;

import lombok.Getter;

import java.util.List;

@Getter
public class NegativeBalanceException extends RuntimeException {
    private final List<String> errors;

    public NegativeBalanceException(String message, List<String> errors) {
        super(message);
        this.errors = errors;
    }

    public NegativeBalanceException(String message) {
        super(message);
        this.errors = List.of(message);
    }
}