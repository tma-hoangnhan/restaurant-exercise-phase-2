package com.tma.restaurant_exercise_phase_2.repository;

import com.tma.restaurant_exercise_phase_2.model.food.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Integer> {
    @Query("SELECT f FROM Food f WHERE f.name = ?1")
    Optional<Food> findFoodByName(String name);

    @Query("SELECT f FROM Food f WHERE f.state = 1")
    Page<Food> getListOfActiveFoods(Pageable pageable);
}
