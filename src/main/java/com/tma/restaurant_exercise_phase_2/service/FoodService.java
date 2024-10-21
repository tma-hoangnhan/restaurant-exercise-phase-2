package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.FoodDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.ItemNameAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import com.tma.restaurant_exercise_phase_2.model.reponsebody.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.repository.FoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodService {
    private final FoodRepository foodRepository;

    @Autowired
    public FoodService(FoodRepository foodRepository) {
        this.foodRepository = foodRepository;
    }

    public CollectionResponse<FoodDTO> getFoodMenu(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        Page<Food> foodPage = foodRepository.getListOfActiveFoods(pageable);

        CollectionResponse<FoodDTO> foodCollectionResponse = new CollectionResponse<>();
        foodCollectionResponse.setPage(foodPage.getNumber() + 1);
        foodCollectionResponse.setPerPage(foodPage.getSize());
        foodCollectionResponse.setTotalPages(foodPage.getTotalPages());
        foodCollectionResponse.setTotalItems(foodPage.getTotalElements());
        foodCollectionResponse.setContents(foodPage.getContent().stream().map(Food::toDTO).collect(Collectors.toList()));

        return foodCollectionResponse;
    }

    public void save(Food food) {
        Optional<Food> optional = findFoodByName(food.getName());
        if (optional.isPresent())
            throw new ItemNameAlreadyExistedException("ITEM WITH NAME " + food.getName() + " HAS ALREADY EXISTED");
        foodRepository.save(food);
    }

    public void update(Food updatedFood) {
        // Check if the requested ID is existed
        Food dbDrink = findById(updatedFood.getId());

        // Check the duplication of the updated item
        if (!updatedFood.getName().equals(dbDrink.getName())) {
            Optional<Food> optional = findFoodByName(updatedFood.getName());
            if (optional.isPresent())
                throw new ItemNameAlreadyExistedException("ITEM WITH NAME " + updatedFood.getName() + " HAS ALREADY EXISTED");
        }
        foodRepository.save(updatedFood);
    }

    public Food findById(int id) {
        return foodRepository
                .findById(id)
                .orElseThrow(() -> new NoItemFoundException("NO ITEM FOUND WITH ID: " + id));
    }

    public Optional<Food> findFoodByName(String name) {
        return foodRepository.findFoodByName(name);
    }

    /**
     * Safe-delete Food by its ID
     */
    public void deleteById(int id) {
        Food food = findById(id);
        food.setState(0);
        foodRepository.save(food);
    }
}
