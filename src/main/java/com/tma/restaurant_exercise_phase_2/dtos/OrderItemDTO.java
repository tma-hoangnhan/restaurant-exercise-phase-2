package com.tma.restaurant_exercise_phase_2.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class OrderItemDTO {
    private int id;
    private ItemDTO item;
    private int billId;
    private int quantity;
}
