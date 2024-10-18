package com.tma.restaurant_exercise_phase_2.model.food;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "breakfast")
public class Breakfast extends Food {
    public Breakfast(String name, String description, String img, double price) {
        super(name, description, img, price);
    }

    public Breakfast(Food food) {
        super(food);
    }

    public Breakfast() {
        this.type = "Breakfast";
    }
}
