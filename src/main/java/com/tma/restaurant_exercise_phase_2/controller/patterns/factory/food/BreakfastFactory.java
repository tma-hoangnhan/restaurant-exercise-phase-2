package com.tma.restaurant_exercise_phase_2.controller.patterns.factory.food;

import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.model.food.Breakfast;
import com.tma.restaurant_exercise_phase_2.model.food.Food;

public class BreakfastFactory implements FoodFactory {
    @Override
    public Food createFood() {
        return new Breakfast();
    }

    @Override
    public Food createFood(ItemDTO itemDTO) {
        Food food = new Breakfast();
        food.setId(itemDTO.getId());
        food.setName(itemDTO.getName());
        food.setDescription(itemDTO.getDescription());
        food.setImg(itemDTO.getImg());
        food.setPrice(itemDTO.getPrice());
        food.setState(1);
        return food;
    }
}
