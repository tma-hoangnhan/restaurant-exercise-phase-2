package com.tma.restaurant_exercise_phase_2.dtos;

import com.tma.restaurant_exercise_phase_2.controller.patterns.factory.DrinkFactory;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class DrinkDTO extends ItemDTO{
    private int id;
    private String name;
    private String description;
    private String img;
    private double price;
    private int state;
    private int volume;
    private String type;

    public Drink toEntity() {
        Drink drink = DrinkFactory.getInstance().createDrink(getType());
        drink.setId(getId());
        drink.setName(getName());
        drink.setDescription(getDescription());
        drink.setImg(getImg());
        drink.setPrice(getPrice());
        drink.setVolume(getVolume());
        return drink;
    }
}
