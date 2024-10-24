package com.tma.restaurant_exercise_phase_2.patterns.factory;

import com.tma.restaurant_exercise_phase_2.exceptions.InvalidItemTypeException;
import com.tma.restaurant_exercise_phase_2.patterns.factory.drink.DrinkFactory;
import com.tma.restaurant_exercise_phase_2.patterns.factory.food.FoodFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class FactoryProviderTest {
    private final FactoryProvider factoryProvider = FactoryProvider.getInstance();

    @Test
    void getInstance() {
        FactoryProvider newCalledFactory = FactoryProvider.getInstance();
        assertEquals(factoryProvider, newCalledFactory);
    }

    @ParameterizedTest
    @ValueSource(strings = {"SoftDrink", "Alcohol"})
    void getDrinkFactory(String type) {
        // when
        DrinkFactory factory = factoryProvider.getDrinkFactory(type);

        // then
        assertTrue(factory.getClass().getSimpleName().contains(type));
    }

    @Test
    void getDrinkFactory_throwInvalidItemTypeException() {
        // given
        String invalidType = "Random Type";

        // when
        InvalidItemTypeException result = assertThrows(
                InvalidItemTypeException.class,
                () -> factoryProvider.getDrinkFactory(invalidType)
        );

        // then
        assertEquals(invalidType + " IS INVALID", result.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Breakfast", "Lunch", "Dinner"})
    void getFoodFactory(String type) {
        // when
        FoodFactory factory = factoryProvider.getFoodFactory(type);

        // then
        assertTrue(factory.getClass().getSimpleName().contains(type));
    }

    @Test
    void getFoodFactory_throwInvalidItemTypeException() {
        // given
        String invalidType = "Random Type";

        // when
        InvalidItemTypeException result = assertThrows(
                InvalidItemTypeException.class,
                () -> factoryProvider.getFoodFactory(invalidType)
        );

        // then
        assertEquals(invalidType + " IS INVALID", result.getMessage());
    }
}
