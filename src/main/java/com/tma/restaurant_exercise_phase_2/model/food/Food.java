package com.tma.restaurant_exercise_phase_2.model.food;

import com.tma.restaurant_exercise_phase_2.dtos.FoodDTO;
import com.tma.restaurant_exercise_phase_2.model.Item;
import lombok.NoArgsConstructor;

import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

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
        return  FoodDTO.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .img(getImg())
                .price(getPrice())
                .state(getState())
                .quantity(getQuantity())
                .type(getType())
                .build();
    }
}
