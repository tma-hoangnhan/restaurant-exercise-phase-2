package com.tma.restaurant_exercise_phase_2.model.drink;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "alcohol")
public class Alcohol extends Drink {
    public Alcohol(String name, String description, String img, double price, int volume) {
        super(name, description, img, price, volume);
    }

    public Alcohol(Drink drink) {
        super(drink);
    }

    public Alcohol() {
        this.type = "Alcohol";
    }
}
