package com.tma.restaurant_exercise_phase_2.model.reponsebody;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CollectionResponse<T> {
    private int page;
    private int perPage;
    private long totalItems;
    private int totalPages;
    private Collection<T> contents;
}
