package com.tma.restaurant_exercise_phase_2.exceptions;

public class ItemAlreadyDeletedException extends IllegalStateException {
    public ItemAlreadyDeletedException(String message) {
        super(message);
    }
}
