package com.tma.restaurant_exercise_phase_2.dtos;

import lombok.*;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CollectionResponse<T> {
    private int page;
    private int perPage;
    private long totalItems;
    private int totalPages;
    private Collection<T> contents;
}
