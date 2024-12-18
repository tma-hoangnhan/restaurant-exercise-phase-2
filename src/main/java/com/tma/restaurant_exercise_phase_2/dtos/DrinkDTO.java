package com.tma.restaurant_exercise_phase_2.dtos;

import com.tma.restaurant_exercise_phase_2.patterns.factory.FactoryProvider;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class DrinkDTO extends ItemDTO{
    public Drink toEntity() {
        Drink drink = FactoryProvider.getInstance().getDrinkFactory(getType()).createDrink();
        drink.setId(getId());
        drink.setName(getName());
        drink.setDescription(getDescription());
        drink.setImg(getImg());
        drink.setPrice(getPrice());
        drink.setVolume(getVolume());
        return drink;
    }
}
