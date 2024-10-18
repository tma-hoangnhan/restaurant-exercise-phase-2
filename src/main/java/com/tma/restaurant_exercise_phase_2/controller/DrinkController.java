package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.controller.patterns.factory.DrinkFactory;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.reponsebody.ListResponse;
import com.tma.restaurant_exercise_phase_2.model.requestbody.RequestDrink;
import com.tma.restaurant_exercise_phase_2.service.DrinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ListResponse<Drink>> getDrinkMenu(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int perPage) {
        if (page < 1 || perPage < 1)
            throw new InvalidParameterException("page AND perPage MUST BE LARGER THAN 0");

        Page<Drink> drinkPage = drinkService.getDrinkMenu(page, perPage);
        return new ResponseEntity<>(
                new ListResponse<>(
                        drinkPage.getNumber() + 1,
                        drinkPage.getSize(),
                        drinkPage.getTotalElements(),
                        drinkPage.getTotalPages(),
                        drinkPage.getContent()
                ),
                HttpStatus.OK
        );
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
