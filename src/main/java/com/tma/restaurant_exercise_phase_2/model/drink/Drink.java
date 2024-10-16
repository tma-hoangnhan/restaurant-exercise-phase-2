package com.tma.restaurant_exercise_phase_2.model.drink;

import com.tma.restaurant_exercise_phase_2.model.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor
public abstract class Drink extends Item {
    protected int volume;

    protected Drink(String name, String description, String img, double price, int volume) {
        super(name, description, img, price);
        this.volume = volume;
    }

    protected Drink(Drink drink) {
        super(drink.id, drink.name, drink.description, drink.img, drink.price);
        this.volume = drink.volume;
    }
}
