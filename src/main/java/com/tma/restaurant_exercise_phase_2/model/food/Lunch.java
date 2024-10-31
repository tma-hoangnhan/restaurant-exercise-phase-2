package com.tma.restaurant_exercise_phase_2.model.food;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "lunch")
public class Lunch extends Food {
    public Lunch(String name, String description, String img, double price) {
        super(name, description, img, price);
    }

    public Lunch(Food food) {
        super(food);
    }

    public Lunch() {
        this.type = "Lunch";
    }
}
