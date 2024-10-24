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
    @Query("SELECT i FROM Item i" +
            " WHERE CONCAT(i.name, i.description) LIKE CONCAT('%', ?1, '%')" +
            " ORDER BY CASE " +
            " WHEN TYPE(i) = SoftDrink THEN 1" +
            " WHEN TYPE(i) = Alcohol THEN 2" +
            " WHEN TYPE(i) = Breakfast THEN 3" +
            " WHEN TYPE(i) = Lunch THEN 4" +
            " WHEN TYPE(i) = Dinner THEN 5" +
            " ELSE 6 END," +
            " i.name ASC, i.price DESC")
    Page<Item> getMenu(Pageable pageable);

    @Query("SELECT d FROM Drink d" +
            " WHERE d.state = 1" +
            " ORDER BY CASE" +
            " WHEN TYPE(d) = SoftDrink THEN 1" +
            " WHEN TYPE(d) = Alcohol THEN 2" +
            " ELSE 3 END," +
            " d.name ASC, d.price DESC")
    Page<Item> getListOfActiveDrinks(Pageable pageable);

    @Query("SELECT f FROM Food f" +
            " WHERE f.state = 1" +
            " ORDER BY CASE" +
            " WHEN TYPE(f) = Breakfast THEN 1" +
            " WHEN TYPE(f) = Lunch THEN 2" +
            " WHEN TYPE(f) = Dinner THEN 3" +
            " ELSE 4 END," +
            " f.name ASC, f.price DESC")
    Page<Item> getListOfActiveFoods(Pageable pageable);

    @Query("SELECT i FROM Item i WHERE i.name = ?1")
    Optional<Item> findItemByName(String name);

    @Query("SELECT i FROM Item i" +
            " WHERE CONCAT(i.name, i.description) LIKE CONCAT('%', ?1, '%')")
    Page<Item> searchItem(String searchString, Pageable pageable);
}
