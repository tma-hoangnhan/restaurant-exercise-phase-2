package com.tma.restaurant_exercise_phase_2.dtos;

import com.tma.restaurant_exercise_phase_2.patterns.factory.FactoryProvider;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class FoodDTO extends ItemDTO {
    public Food toEntity() {
        Food food = FactoryProvider.getInstance().getFoodFactory(getType()).createFood();
        food.setId(getId());
        food.setName(getName());
        food.setDescription(getDescription());
        food.setImg(getImg());
        food.setPrice(getPrice());
        return food;
    }
}
