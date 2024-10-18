package com.tma.restaurant_exercise_phase_2.patterns;

import com.tma.restaurant_exercise_phase_2.controller.patterns.factory.DrinkFactory;
import com.tma.restaurant_exercise_phase_2.exceptions.InvalidItemTypeException;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.requestbody.RequestDrink;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DrinkFactoryTest {
    DrinkFactory drinkFactory = DrinkFactory.getInstance();

    @ParameterizedTest
    @ValueSource(strings = {"SoftDrink", "Alcohol"})
    void createDrink_success(String type) {
        Drink drink = drinkFactory.createDrink(type);
        Assertions.assertEquals(type, drink.getClass().getSimpleName());
    }

    @Test
    void createDrink_invalidItemType() {
        String invalidType = "Invalid Type";

        InvalidItemTypeException result = Assertions.assertThrows(
                InvalidItemTypeException.class,
                () -> drinkFactory.createDrink(invalidType)
        );
        Assertions.assertEquals("TYPE " + invalidType + " IS INVALID", result.getMessage());
    }

    @Test
    void createDrink_invalidNullType() {
        InvalidItemTypeException result = Assertions.assertThrows(
                InvalidItemTypeException.class,
                () -> drinkFactory.createDrink((String) null)
        );
        Assertions.assertEquals("TYPE NULL IS INVALID", result.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"SoftDrink", "Alcohol"})
    void createDrink_withObject_success(String type) {
        RequestDrink requestDrink = new RequestDrink("Drink", "Drink", "URL", 10, 500, type);

        Drink drink = drinkFactory.createDrink(requestDrink);
        Assertions.assertEquals(type, drink.getClass().getSimpleName());
    }

    @Test
    void createDrink_withObject_invalidItemType() {
        String invalidType = "Invalid Type";

        RequestDrink requestDrink = new RequestDrink("Drink", "Drink", "URL", 10, 500, invalidType);
        InvalidItemTypeException result = Assertions.assertThrows(
                InvalidItemTypeException.class,
                () -> drinkFactory.createDrink(requestDrink)
        );
        Assertions.assertEquals("TYPE " + invalidType + " IS INVALID", result.getMessage());
    }
    @Test
    void createDrink_withObject_invalidNullType() {
        RequestDrink requestDrink = new RequestDrink("Drink", "Drink", "URL", 10, 500, null);
        InvalidItemTypeException result = Assertions.assertThrows(
                InvalidItemTypeException.class,
                () -> drinkFactory.createDrink(requestDrink)
        );
        Assertions.assertEquals("TYPE NULL IS INVALID", result.getMessage());
    }
}
