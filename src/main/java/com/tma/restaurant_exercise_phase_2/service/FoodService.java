package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.exceptions.ItemNameAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import com.tma.restaurant_exercise_phase_2.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    @Autowired
    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public List<Food> getFoodMenu() {
        return foodRepository.findAll();
    }

    public void save(Food food) {
        Optional<Food> optional = findFoodByName(food.getName());
        if (optional.isPresent())
            throw new ItemNameAlreadyExistedException("ITEM WITH NAME " + food.getName() + " HAS ALREADY EXISTED");
        foodRepository.save(food);
    }

    public Food findById(int id) {
        return foodRepository
                .findById(id)
                .orElseThrow(() -> new NoItemFoundException("NO ITEM FOUND WITH ID: " + id));
    }

    public Optional<Food> findFoodByName(String name) {
        return foodRepository.findFoodByName(name);
    }

    public void deleteById(int id) {
        findById(id);
        foodRepository.deleteById(id);
    }
}
