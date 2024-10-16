package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.exceptions.ItemNameAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.repository.DrinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DrinkService {
    private final DrinkRepository drinkRepository;

    @Autowired
    public DrinkService(DrinkRepository drinkRepository) {
        this.drinkRepository = drinkRepository;
    }

    public List<Drink> getDrinkMenu() {
        return drinkRepository.findAll();
    }

    public void save(Drink drink) {
        Optional<Drink> optional = findDrinkByName(drink.getName());
        if (optional.isPresent())
            throw new ItemNameAlreadyExistedException("ITEM WITH NAME " + drink.getName() + " HAS ALREADY EXISTED");
        drinkRepository.save(drink);
    }

    public void update(Drink updatedDrink) {
        // Check if the requested ID is existed
        Drink dbDrink = findById(updatedDrink.getId());

        // Check the duplication of the updated item
        if (!updatedDrink.getName().equals(dbDrink.getName())) {
            Optional<Drink> optional = findDrinkByName(updatedDrink.getName());
            if (optional.isPresent())
                throw new ItemNameAlreadyExistedException("ITEM WITH NAME " + updatedDrink.getName() + " HAS ALREADY EXISTED");
        }
        drinkRepository.save(updatedDrink);
    }

    public Drink findById(int id) {
        return drinkRepository
                .findById(id)
                .orElseThrow(()-> new NoItemFoundException("NO ITEM FOUND WITH ID: " + id));
    }

    public Optional<Drink> findDrinkByName(String name) {
        return drinkRepository.findDrinkByName(name);
    }

    public void deleteById(int id) {
        findById(id);
        drinkRepository.deleteById(id);
    }
}
