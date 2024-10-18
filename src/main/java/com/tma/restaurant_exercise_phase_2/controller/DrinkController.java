package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.controller.patterns.factory.DrinkFactory;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.requestbody.RequestDrink;
import com.tma.restaurant_exercise_phase_2.service.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/drink")
public class DrinkController {
    private final DrinkService drinkService;

    @Autowired
    public DrinkController(DrinkService drinkService) {
        this.drinkService = drinkService;
    }

    @GetMapping
    public ResponseEntity<List<Drink>> getDrinkMenu() {
        List<Drink> drinks = drinkService.getDrinkMenu();
        return new ResponseEntity<>(drinks, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> createNewDrink(@RequestBody RequestDrink requestDrink) {
        Drink newDrink = DrinkFactory.getInstance().createDrink(requestDrink);
        drinkService.save(newDrink);
        return new ResponseEntity<>("Created", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<String> updateDrink(@RequestBody RequestDrink requestDrink) {
        Drink updatedDrink = DrinkFactory.getInstance().createDrink(requestDrink);
        drinkService.update(updatedDrink);
        return new ResponseEntity<>("Updated", HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteDrinkById(@RequestParam int id) {
        drinkService.deleteById(id);
        return new ResponseEntity<>("Deleted", HttpStatus.OK);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Drink> getDrinkById(@PathVariable("id") int id) {
        return new ResponseEntity<>(drinkService.findById(id), HttpStatus.OK);
    }
}
