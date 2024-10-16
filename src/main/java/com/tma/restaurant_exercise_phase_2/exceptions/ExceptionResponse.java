package com.tma.restaurant_exercise_phase_2.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionResponse extends ResponseEntity<String> {
    public ExceptionResponse(String body, HttpStatus status) {
        super(body, status);
    }
}
