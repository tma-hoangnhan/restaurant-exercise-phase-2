package com.tma.restaurant_exercise_phase_2.dtos;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class BillDetailsDTO extends BillDTO{
    private List<OrderItemDTO> orderItemList;

}
