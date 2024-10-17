package com.tma.restaurant_exercise_phase_2.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {

    private JsonUtils() {
        throw new IllegalStateException("Cannot instantiate JsonUtils");
    }

    public static String writeJsonString(Object obj) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(obj);
    }
}
