package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.dtos.FoodDTO;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;

@RestController
@RequestMapping(path = "/food")
public class FoodController {
    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public ResponseEntity<CollectionResponse<FoodDTO>> getFoodMenu(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int perPage) {
        if (page < 1 || perPage < 1)
            throw new InvalidParameterException("page AND perPage MUST BE LARGER THAN 0");

        return new ResponseEntity<>(
                foodService.getFoodMenu(page, perPage),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<String> createNewFood(@RequestBody FoodDTO foodDTO) {
        Food newFood = foodDTO.toEntity();
        foodService.save(newFood);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateFood(@RequestBody FoodDTO foodDTO) {
        Food updatedFood = foodDTO.toEntity();
        updatedFood.setState(foodDTO.getState());
        foodService.update(updatedFood);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteFoodById(@RequestParam int id) {
        foodService.deleteById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Food> getFoodById(@PathVariable("id") int id) {
        return new ResponseEntity<>(foodService.findById(id), HttpStatus.OK);
    }
}
