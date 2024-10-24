package com.tma.restaurant_exercise_phase_2.patterns.factory.drink;

import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.model.drink.Alcohol;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;

public class AlcoholFactory implements DrinkFactory {
    @Override
    public Drink createDrink(){
        return new Alcohol();
    }

    @Override
    public Drink createDrink(ItemDTO itemDTO) {
        Drink drink = new Alcohol();
        drink.setId(itemDTO.getId());
        drink.setName(itemDTO.getName());
        drink.setDescription(itemDTO.getDescription());
        drink.setImg(itemDTO.getImg());
        drink.setPrice(itemDTO.getPrice());
        drink.setVolume(itemDTO.getVolume());
        drink.setState(1);
        return drink;
    }
}
