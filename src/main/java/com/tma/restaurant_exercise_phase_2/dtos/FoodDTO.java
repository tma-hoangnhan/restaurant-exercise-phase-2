package com.tma.restaurant_exercise_phase_2.dtos;

import com.tma.restaurant_exercise_phase_2.controller.patterns.factory.FoodFactory;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class FoodDTO extends ItemDTO {
    private int id;
    private String name;
    private String description;
    private String img;
    private double price;
    private String type;
    private int state;

    public Food toEntity() {
        Food food = FoodFactory.getInstance().createFood(getType());
        food.setId(getId());
        food.setName(getName());
        food.setDescription(getDescription());
        food.setImg(getImg());
        food.setPrice(getPrice());
        return food;
    }
}
