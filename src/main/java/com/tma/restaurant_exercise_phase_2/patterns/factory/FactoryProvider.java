package com.tma.restaurant_exercise_phase_2.patterns.factory;

import com.tma.restaurant_exercise_phase_2.patterns.factory.drink.AlcoholFactory;
import com.tma.restaurant_exercise_phase_2.patterns.factory.drink.DrinkFactory;
import com.tma.restaurant_exercise_phase_2.patterns.factory.drink.SoftDrinkFactory;
import com.tma.restaurant_exercise_phase_2.patterns.factory.food.BreakfastFactory;
import com.tma.restaurant_exercise_phase_2.patterns.factory.food.DinnerFactory;
import com.tma.restaurant_exercise_phase_2.patterns.factory.food.FoodFactory;
import com.tma.restaurant_exercise_phase_2.patterns.factory.food.LunchFactory;
import com.tma.restaurant_exercise_phase_2.exceptions.InvalidItemTypeException;

public class FactoryProvider {
    private static FactoryProvider instance = null;
    private FactoryProvider() {}

    public static FactoryProvider getInstance() {
        if (instance == null) instance = new FactoryProvider();
        return instance;
    }

    public DrinkFactory getDrinkFactory(String type) {
        if (type.equalsIgnoreCase("SOFTDRINK"))
            return new SoftDrinkFactory();
        else if (type.equalsIgnoreCase("ALCOHOL"))
            return new AlcoholFactory();

        throw new InvalidItemTypeException(type + " IS INVALID");
    }

    public FoodFactory getFoodFactory(String type) {
        if (type.equalsIgnoreCase("BREAKFAST"))
            return new BreakfastFactory();
        else if (type.equalsIgnoreCase("LUNCH"))
            return new LunchFactory();
        else if (type.equalsIgnoreCase("DINNER"))
            return new DinnerFactory();

        throw new InvalidItemTypeException(type + " IS INVALID");
    }
}
