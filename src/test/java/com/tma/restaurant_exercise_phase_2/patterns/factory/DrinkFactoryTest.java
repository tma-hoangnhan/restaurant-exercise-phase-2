package com.tma.restaurant_exercise_phase_2.patterns.factory;

import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DrinkFactoryTest {
    @ParameterizedTest
    @ValueSource(strings = {"SoftDrink", "Alcohol"})
    void createDrink(String type) {
        // when
        Drink drink = FactoryProvider.getInstance().getDrinkFactory(type).createDrink();

        // then
        assertEquals(type, drink.getClass().getSimpleName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"SoftDrink", "Alcohol"})
    void createDrinkWithDTO(String type) {
        // given
        ItemDTO expected = ItemDTO
                .builder()
                .name("Test Drink")
                .description("Test Drink")
                .img("Image URL")
                .price(10)
                .volume(500)
                .type(type)
                .build();

        // when
        Drink actual = FactoryProvider.getInstance().getDrinkFactory(type).createDrink(expected);

        // then
        assertEquals(type, actual.getClass().getSimpleName());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getImg(), actual.getImg());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getVolume(), actual.getVolume());
        assertEquals(expected.getType(), actual.getType());
    }
}
