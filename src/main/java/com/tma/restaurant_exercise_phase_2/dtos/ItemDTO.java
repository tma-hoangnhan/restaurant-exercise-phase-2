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
    private int id;
    private String name;
    private String description;
    private String img;
    private double price;
    private String type;
    private int state;
    private int volume;
    private boolean isDrink;
}
