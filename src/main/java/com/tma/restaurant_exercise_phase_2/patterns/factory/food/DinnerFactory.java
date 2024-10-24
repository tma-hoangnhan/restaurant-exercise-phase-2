package com.tma.restaurant_exercise_phase_2.patterns.factory.food;

import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.model.food.Dinner;
import com.tma.restaurant_exercise_phase_2.model.food.Food;

public class DinnerFactory implements FoodFactory {
    @Override
    public Food createFood() {
        return new Dinner();
    }

    @Override
    public Food createFood(ItemDTO itemDTO) {
        Food food = new Dinner();
        food.setId(itemDTO.getId());
        food.setName(itemDTO.getName());
        food.setDescription(itemDTO.getDescription());
        food.setImg(itemDTO.getImg());
        food.setPrice(itemDTO.getPrice());
        food.setState(1);
        return food;
    }
}
