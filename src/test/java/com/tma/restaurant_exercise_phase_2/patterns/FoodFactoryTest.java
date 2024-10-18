package com.tma.restaurant_exercise_phase_2.patterns;

import com.tma.restaurant_exercise_phase_2.controller.patterns.factory.FoodFactory;
import com.tma.restaurant_exercise_phase_2.exceptions.InvalidItemTypeException;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import com.tma.restaurant_exercise_phase_2.model.requestbody.RequestFood;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class FoodFactoryTest {
    FoodFactory foodFactory = FoodFactory.getInstance();

    @ParameterizedTest
    @ValueSource(strings = {"Breakfast", "Lunch", "Dinner"})
    void createFood_success(String type) {
        Food food = foodFactory.createFood(type);
        Assertions.assertEquals(type, food.getClass().getSimpleName());
    }

    @Test
    void createFood_invalidItemType() {
        String invalidType = "Invalid Type";
        InvalidItemTypeException result = Assertions.assertThrows(
                InvalidItemTypeException.class,
                () -> foodFactory.createFood(invalidType)
        );
        Assertions.assertEquals("TYPE " + invalidType + " IS INVALID", result.getMessage());
    }
    @Test
    void createFood_invalidNullType() {
        InvalidItemTypeException result = Assertions.assertThrows(
                InvalidItemTypeException.class,
                () -> foodFactory.createFood((String) null)
        );
        Assertions.assertEquals("TYPE NULL IS INVALID", result.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Breakfast", "Lunch", "Dinner"})
    void createFood_withObject_success(String type) {
        RequestFood requestFood = new RequestFood("Food", "Food", "URL", 30, type);
        Food food = foodFactory.createFood(requestFood);
        Assertions.assertEquals(type, food.getClass().getSimpleName());
    }

    @Test
    void createFood_withObject_invalidItemType() {
        String invalidType = "Invalid Type";
        RequestFood requestFood = new RequestFood("Food", "Food", "URL", 30, invalidType);

        InvalidItemTypeException result = Assertions.assertThrows(
                InvalidItemTypeException.class,
                () -> foodFactory.createFood(requestFood)
        );
        Assertions.assertEquals("TYPE " + invalidType + " IS INVALID", result.getMessage());
    }

    @Test
    void createFood_withObject_invalidNullType() {
        RequestFood requestFood = new RequestFood("Food", "Food", "URL", 30, null);

        InvalidItemTypeException result = Assertions.assertThrows(
                InvalidItemTypeException.class,
                () -> foodFactory.createFood(requestFood)
        );
        Assertions.assertEquals("TYPE NULL IS INVALID", result.getMessage());
    }
}
