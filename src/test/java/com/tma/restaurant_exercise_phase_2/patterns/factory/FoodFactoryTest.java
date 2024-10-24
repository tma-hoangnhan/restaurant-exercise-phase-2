package com.tma.restaurant_exercise_phase_2.patterns.factory;

import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FoodFactoryTest {
    @ParameterizedTest
    @ValueSource(strings = {"Breakfast", "Lunch", "Dinner"})
    void createFood(String type) {
        // when
        Food food = FactoryProvider.getInstance().getFoodFactory(type).createFood();

        // then
        assertEquals(type, food.getClass().getSimpleName());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Breakfast", "Lunch", "Dinner"})
    void createFoodWithDTO(String type) {
        // given
        ItemDTO expected = ItemDTO
                .builder()
                .name("Test Food")
                .description("Test Food")
                .img("Image URL")
                .price(10)
                .type(type)
                .build();

        // when
        Food actual = FactoryProvider.getInstance().getFoodFactory(type).createFood(expected);

        // then
        assertEquals(type, actual.getClass().getSimpleName());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getImg(), actual.getImg());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getType(), actual.getType());
    }
}
