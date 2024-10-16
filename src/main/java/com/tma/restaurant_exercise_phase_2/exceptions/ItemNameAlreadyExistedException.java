package com.tma.restaurant_exercise_phase_2.exceptions;

public class ItemNameAlreadyExistedException extends RuntimeException {
    public ItemNameAlreadyExistedException(String message) {
        super(message);
    }
}
