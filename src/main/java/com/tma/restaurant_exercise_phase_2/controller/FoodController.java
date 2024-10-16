package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.model.food.Breakfast;
import com.tma.restaurant_exercise_phase_2.model.food.Dinner;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import com.tma.restaurant_exercise_phase_2.model.food.Lunch;
import com.tma.restaurant_exercise_phase_2.model.requestbody.RequestFood;
import com.tma.restaurant_exercise_phase_2.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@RequestMapping(path = "/food")
public class FoodController {
    private final FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping
    public ResponseEntity<List<Food>> getFoodMenu() {
        return new ResponseEntity<>(foodService.getFoodMenu(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createNewFood(@RequestBody RequestFood requestFood) {
        String type = requestFood.getType();

        Food newFood;
        switch (type) {
            case "Breakfast":
                newFood = new Breakfast(requestFood);
                break;
            case "Lunch":
                newFood = new Lunch(requestFood);
                break;
            case "Dinner":
                newFood = new Dinner(requestFood);
                break;
            default:
                throw new InvalidParameterException("Type " + type + " is invalid");
        }

        foodService.save(newFood);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
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
