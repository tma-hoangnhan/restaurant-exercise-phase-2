package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.controller.patterns.factory.FoodFactory;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import com.tma.restaurant_exercise_phase_2.model.reponsebody.ListResponse;
import com.tma.restaurant_exercise_phase_2.model.requestbody.RequestFood;
import com.tma.restaurant_exercise_phase_2.service.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ListResponse<Food>> getFoodMenu(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int perPage) {
        if (page < 1 || perPage < 1)
            throw new InvalidParameterException("page AND perPage MUST BE LARGER THAN 0");

        Page<Food> foodPage = foodService.getFoodMenu(page, perPage);
        return new ResponseEntity<>(
                new ListResponse<>(
                        foodPage.getNumber() + 1,
                        foodPage.getSize(),
                        foodPage.getTotalElements(),
                        foodPage.getTotalPages(),
                        foodPage.getContent()
                ),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<String> createNewFood(@RequestBody RequestFood requestFood) {
        Food newFood = FoodFactory.getInstance().createFood(requestFood);
        foodService.save(newFood);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateFood(@RequestBody RequestFood requestFood) {
        Food updatedFood = FoodFactory.getInstance().createFood(requestFood);
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
