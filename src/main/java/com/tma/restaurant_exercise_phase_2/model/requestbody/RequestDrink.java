package com.tma.restaurant_exercise_phase_2.model.requestbody;

import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestDrink extends Drink {
    public RequestDrink(String name, String description, String img, double price, int volume, String type) {
        super(name, description, img, price, volume);
        this.type = type;
    }
}
