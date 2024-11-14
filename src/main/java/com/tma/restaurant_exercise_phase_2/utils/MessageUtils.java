package com.tma.restaurant_exercise_phase_2.utils;

public class MessageUtils {
    public static final String INVALID_PAGINATION = "page AND perPage MUST BE LARGER THAN 0";

    private MessageUtils() {
        throw new IllegalStateException("Cannot instantiate MessageUtils");
    }
}
