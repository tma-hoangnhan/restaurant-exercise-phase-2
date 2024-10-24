package com.tma.restaurant_exercise_phase_2.factory.drink;

import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;

public interface DrinkFactory {
    Drink createDrink();
    Drink createDrink(ItemDTO itemDTO);

}
