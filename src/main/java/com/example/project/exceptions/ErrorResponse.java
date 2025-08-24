package com.example.project.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ErrorResponse {
    private Instant timestamp;
    private String error;
    private String message;
}
