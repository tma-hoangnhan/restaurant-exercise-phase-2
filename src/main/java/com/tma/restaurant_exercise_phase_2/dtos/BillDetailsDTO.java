package com.tma.restaurant_exercise_phase_2.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class BillDetailsDTO extends BillDTO{
    private List<OrderItemDTO> orderItemList;

}
