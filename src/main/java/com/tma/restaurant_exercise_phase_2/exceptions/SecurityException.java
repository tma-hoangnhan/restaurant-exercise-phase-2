package com.tma.restaurant_exercise_phase_2.exceptions;

import org.springframework.security.core.AuthenticationException;

public class SecurityException extends AuthenticationException {
    public SecurityException(String message) {
        super(message);
    }
}
