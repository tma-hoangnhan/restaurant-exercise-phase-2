package com.tma.restaurant_exercise_phase_2.model.drink;

import javax.persistence.Entity;

@Entity
public class SoftDrink extends Drink {
    public SoftDrink(String name, String description, String img, double price, int volume) {
        super(name, description, img, price, volume);
        this.type = "SoftDrink";
    }

    public SoftDrink(Drink drink) {
        super(drink);
    }

    public SoftDrink() {
        this.type = "SoftDrink";
    }
}
