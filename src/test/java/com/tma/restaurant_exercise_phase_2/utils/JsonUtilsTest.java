package com.tma.restaurant_exercise_phase_2.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.drink.SoftDrink;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class JsonUtilsTest {
    @Test
    void writeJsonString() throws JsonProcessingException {
        // given
        Drink sampleObject = new SoftDrink("Test Drink", "Test Drink", "Image URL", 10, 500);
        sampleObject.setId(1);

        String expected = "{\"id\":1,\"name\":\"Test Drink\",\"description\":\"Test Drink\",\"img\":\"Image URL\"," +
                "\"price\":10.0,\"state\":1,\"type\":\"SoftDrink\",\"volume\":500}";

        // when
        String actual = JsonUtils.writeJsonString(sampleObject);

        // then
        Assertions.assertEquals(expected, actual);
    }
}
