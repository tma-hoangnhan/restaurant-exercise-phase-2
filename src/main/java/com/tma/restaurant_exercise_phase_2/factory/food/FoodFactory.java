package com.tma.restaurant_exercise_phase_2.factory.food;

import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.model.food.Food;

public interface FoodFactory {
    Food createFood();
    Food createFood(ItemDTO itemDTO);
}
