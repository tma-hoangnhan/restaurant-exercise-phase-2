package com.tma.restaurant_exercise_phase_2.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class BillDTO {
    private int id;
    private LocalDateTime orderedTime;
    private double totalPrice;
}
