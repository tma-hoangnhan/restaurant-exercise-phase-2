package com.tma.restaurant_exercise_phase_2.dtos;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class FilterRequest {
    private List<String> itemTypes;
}
