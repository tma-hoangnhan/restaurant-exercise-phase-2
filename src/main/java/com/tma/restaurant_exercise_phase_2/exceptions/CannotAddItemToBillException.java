package com.tma.restaurant_exercise_phase_2.exceptions;

public class CannotAddItemToBillException extends IllegalArgumentException {
    public CannotAddItemToBillException(String message) {
        super(message);
    }
}
