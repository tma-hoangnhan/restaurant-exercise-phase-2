package com.tma.restaurant_exercise_phase_2.dtos;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class OrderItemDTO {
    private int id;
    private ItemDTO item;
    private int billId;
    private int quantity;
}
