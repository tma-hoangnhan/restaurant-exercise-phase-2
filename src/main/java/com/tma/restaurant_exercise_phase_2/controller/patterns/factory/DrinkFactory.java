package com.tma.restaurant_exercise_phase_2.controller.patterns.factory;

import com.tma.restaurant_exercise_phase_2.exceptions.InvalidItemTypeException;
import com.tma.restaurant_exercise_phase_2.model.drink.Alcohol;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.drink.SoftDrink;
import com.tma.restaurant_exercise_phase_2.model.requestbody.RequestDrink;

public class DrinkFactory {
    private static DrinkFactory instance;
    private DrinkFactory() {}

    public static DrinkFactory getInstance() {
        if (instance == null) instance = new DrinkFactory();
        return instance;
    }

    public Drink createDrink(String type) {
        if (type == null) return null;

        if(type.equalsIgnoreCase("SOFTDRINK"))
            return new SoftDrink();
        else if (type.equalsIgnoreCase("ALCOHOL"))
            return new Alcohol();

        throw new InvalidItemTypeException("TYPE " + type + " IS INVALID");
    }

    public Drink createDrink(RequestDrink requestDrink) {
        String type = requestDrink.getType();

        if (type == null) return null;

        if(type.equalsIgnoreCase("SOFTDRINK"))
            return new SoftDrink(requestDrink);
        else if (type.equalsIgnoreCase("ALCOHOL"))
            return new Alcohol(requestDrink);

        throw new InvalidItemTypeException("TYPE " + type + " IS INVALID");
    }
}
