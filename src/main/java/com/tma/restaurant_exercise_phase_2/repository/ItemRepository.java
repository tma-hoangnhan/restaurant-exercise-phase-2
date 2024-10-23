package com.tma.restaurant_exercise_phase_2.repository;

import com.tma.restaurant_exercise_phase_2.model.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    @Query("SELECT i FROM Item i")
    Page<Item> getMenu(Pageable pageable);

    @Query("SELECT d FROM Drink d WHERE d.state = 1")
    Page<Item> getListOfActiveDrinks(Pageable pageable);

    @Query("SELECT f FROM Food f WHERE f.state = 1")
    Page<Item> getListOfActiveFoods(Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.name = ?1")
    Optional<Item> findItemByName(String name);
}
