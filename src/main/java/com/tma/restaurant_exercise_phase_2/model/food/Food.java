package com.tma.restaurant_exercise_phase_2.model.food;

import com.tma.restaurant_exercise_phase_2.dtos.FoodDTO;
import com.tma.restaurant_exercise_phase_2.model.Item;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@Table(name = "Food")
public abstract class Food extends Item {
    protected Food(String name, String description, String img, double price) {
        super(name, description, img, price);
    }

    protected Food(Food food) {
        super(food.id, food.name, food.description, food.img, food.price);
    }

    @Override
    public FoodDTO toDTO() {
        FoodDTO foodDTO = new FoodDTO();
        foodDTO.setId(getId());
        foodDTO.setName(getName());
        foodDTO.setDescription(getDescription());
        foodDTO.setImg(getImg());
        foodDTO.setPrice(getPrice());
        foodDTO.setState(getState());
        foodDTO.setType(getType());
        return foodDTO;
    }
}
