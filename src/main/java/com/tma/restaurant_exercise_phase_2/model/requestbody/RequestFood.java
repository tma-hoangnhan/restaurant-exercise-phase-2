package com.tma.restaurant_exercise_phase_2.model.requestbody;

import com.tma.restaurant_exercise_phase_2.model.food.Food;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RequestFood extends Food {
    public RequestFood(String name, String description, String img, double price, String type) {
        super(name, description, img, price);
        this.type = type;
    }
}
