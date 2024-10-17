package com.tma.restaurant_exercise_phase_2.controller.patterns.factory;

import com.tma.restaurant_exercise_phase_2.exceptions.InvalidItemTypeException;
import com.tma.restaurant_exercise_phase_2.model.food.Breakfast;
import com.tma.restaurant_exercise_phase_2.model.food.Dinner;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import com.tma.restaurant_exercise_phase_2.model.food.Lunch;
import com.tma.restaurant_exercise_phase_2.model.requestbody.RequestFood;

public class FoodFactory {
    private static FoodFactory instance;

    private FoodFactory() {}

    public static FoodFactory getInstance() {
        if (instance == null) instance = new FoodFactory();
        return instance;
    }

    public Food createFood(String type) {
        if (type == null) return null;

        if(type.equalsIgnoreCase("BREAKFAST"))
            return new Breakfast();
        else if (type.equalsIgnoreCase("LUNCH"))
            return new Lunch();
        else if (type.equalsIgnoreCase("DINNER"))
            return new Dinner();

        throw new InvalidItemTypeException("TYPE " + type + " IS INVALID");
    }

    public Food createFood(RequestFood requestFood) {
        String type = requestFood.getType();
        if (type == null) return null;

        if(type.equalsIgnoreCase("BREAKFAST"))
            return new Breakfast(requestFood);
        else if (type.equalsIgnoreCase("LUNCH"))
            return new Lunch(requestFood);
        else if (type.equalsIgnoreCase("DINNER"))
            return new Dinner(requestFood);

        throw new InvalidItemTypeException("TYPE " + type + " IS INVALID");
    }
}
