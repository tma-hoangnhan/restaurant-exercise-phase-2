package com.tma.restaurant_exercise_phase_2.exceptions;

public class UserAlreadyExistedException extends RuntimeException{
    public UserAlreadyExistedException(String message) {
        super(message);
    }
}
