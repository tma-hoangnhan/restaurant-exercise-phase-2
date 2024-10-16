package com.tma.restaurant_exercise_phase_2.exceptions;

public class InvalidItemTypeException extends IllegalArgumentException{
    public InvalidItemTypeException(String message) {
        super(message);
    }
}
