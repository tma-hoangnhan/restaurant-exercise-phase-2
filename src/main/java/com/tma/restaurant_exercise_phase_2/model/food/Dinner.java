package com.tma.restaurant_exercise_phase_2.model.food;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "dinner")
public class Dinner extends Food {
    public Dinner(String name, String description, String img, double price) {
        super(name, description, img, price);
    }

    public Dinner(Food food) {
        super(food);
    }

    public Dinner() {
        this.type = "Dinner";
    }
}
