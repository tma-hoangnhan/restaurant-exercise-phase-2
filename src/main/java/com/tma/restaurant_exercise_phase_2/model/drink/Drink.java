package com.tma.restaurant_exercise_phase_2.model.drink;

import com.tma.restaurant_exercise_phase_2.dtos.DrinkDTO;
import com.tma.restaurant_exercise_phase_2.model.Item;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@Setter
@NoArgsConstructor
@Table(name = "drink")
public abstract class Drink extends Item {
    @Column(name = "volume")
    protected int volume;

    protected Drink(String name, String description, String img, double price, int volume) {
        super(name, description, img, price);
        this.volume = volume;
    }

    protected Drink(Drink drink) {
        super(drink.id, drink.name, drink.description, drink.img, drink.price);
        this.volume = drink.volume;
    }

    @Override
    public DrinkDTO toDTO() {
        return DrinkDTO
                .builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .img(getImg())
                .price(getPrice())
                .state(getState())
                .volume(getVolume())
                .type(getType())
                .build();
    }
}
