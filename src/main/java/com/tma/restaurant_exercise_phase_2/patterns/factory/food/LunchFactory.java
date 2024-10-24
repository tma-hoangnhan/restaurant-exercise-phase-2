package com.tma.restaurant_exercise_phase_2.patterns.factory.food;

import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import com.tma.restaurant_exercise_phase_2.model.food.Lunch;

public class LunchFactory implements FoodFactory {
    @Override
    public Food createFood() {
        return new Lunch();
    }

    @Override
    public Food createFood(ItemDTO itemDTO) {
        Food food = new Lunch();
        food.setId(itemDTO.getId());
        food.setName(itemDTO.getName());
        food.setDescription(itemDTO.getDescription());
        food.setImg(itemDTO.getImg());
        food.setPrice(itemDTO.getPrice());
        food.setState(1);
        return food;
    }
}
