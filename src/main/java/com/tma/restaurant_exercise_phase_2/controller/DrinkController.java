package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.dtos.DrinkDTO;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.reponsebody.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.service.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;

@RestController
@RequestMapping(path = "/drink")
public class DrinkController {
    private final DrinkService drinkService;

    @Autowired
    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping
    public ResponseEntity<CollectionResponse<DrinkDTO>> getDrinkMenu(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int perPage) {
        if (page < 1 || perPage < 1)
            throw new InvalidParameterException("page AND perPage MUST BE LARGER THAN 0");

        return new ResponseEntity<>(
                drinkService.getDrinkMenu(page, perPage),
                HttpStatus.OK
        );
    }

    @PostMapping
    public ResponseEntity<String> createNewDrink(@RequestBody DrinkDTO drinkDTO) {
        Drink newDrink = drinkDTO.toEntity();
        drinkService.save(newDrink);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateDrink(@RequestBody DrinkDTO drinkDTO) {
        Drink updatedDrink = drinkDTO.toEntity();
        updatedDrink.setState(drinkDTO.getState());

        drinkService.update(updatedDrink);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteDrinkById(@RequestParam int id) {
        drinkService.deleteById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<DrinkDTO> getDrinkById(@PathVariable("id") int id) {
        return new ResponseEntity<>(drinkService.findById(id).toDTO(), HttpStatus.OK);
    }
}
