package com.tma.restaurant_exercise_phase_2.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class ItemDTO {
    protected int id;
    protected String name;
    protected String description;
    protected String img;
    protected double price;
    protected String type;
    protected int state;
    protected int quantity;
    protected int volume;
    protected boolean isDrink;
}
