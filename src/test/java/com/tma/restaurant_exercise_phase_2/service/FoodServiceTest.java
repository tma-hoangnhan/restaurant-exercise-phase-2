package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.exceptions.ItemNameAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.food.Breakfast;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import com.tma.restaurant_exercise_phase_2.repository.FoodRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class FoodServiceTest {
    private FoodService foodService;

    @Mock
    private FoodRepository foodRepository;

    private Food expected;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        foodService = new FoodService(foodRepository);

        expected = new Breakfast("Test Breakfast", "Test Breakfast", "URL for Test Breakfast img", 30);
        expected.setId(1);
    }

    @Test
    void testGetFoodMenu() {
        foodService.getFoodMenu();
        Mockito.verify(foodRepository).findAll();
    }

    @Test
    void testSave_success() {
        foodService.save(expected);
        Mockito.verify(foodRepository).save(expected);
    }

    @Test
    void testSave_itemNameHasAlreadyExisted() {
        Mockito.when(foodRepository.findFoodByName(expected.getName())).thenReturn(Optional.of(expected));
        ItemNameAlreadyExistedException result = Assertions.assertThrows(
                ItemNameAlreadyExistedException.class,
                () -> foodService.save(expected)
        );
        Assertions.assertEquals("ITEM WITH NAME " + expected.getName() + " HAS ALREADY EXISTED", result.getMessage());
    }

    @Test
    void testFindById_found() {
        Mockito.when(foodRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Food actual = foodService.findById(expected.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getImg(), actual.getImg());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    void testFindById_notFound() {
        NoItemFoundException result = Assertions.assertThrows(NoItemFoundException.class, () -> foodService.findById(999));
        Assertions.assertEquals("NO ITEM FOUND WITH ID: 999", result.getMessage());
    }

    @Test
    void testDeleteById_success() {
        Mockito.when(foodRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        foodService.deleteById(expected.getId());
        Mockito.verify(foodRepository).deleteById(expected.getId());
    }
}
