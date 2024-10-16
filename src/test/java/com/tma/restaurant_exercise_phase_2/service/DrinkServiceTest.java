package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.exceptions.ItemNameAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.drink.SoftDrink;
import com.tma.restaurant_exercise_phase_2.repository.DrinkRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class DrinkServiceTest {
    private DrinkService drinkService;

    @Mock
    private DrinkRepository drinkRepository;

    Drink expected;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        drinkService = new DrinkService(drinkRepository);

        expected = new SoftDrink("Test Drink", "Test Drink", "Image URL for Test Drink", 10, 500);
        expected.setId(1);
    }

    @Test
    void testSave_success() {
        drinkService.save(expected);
        Mockito.verify(drinkRepository).save(expected);
    }

    @Test
    void testSave_itemNameHasAlreadyExisted() {
        Mockito.when(drinkRepository.findDrinkByName(expected.getName())).thenReturn(Optional.of(expected));
        ItemNameAlreadyExistedException result = Assertions.assertThrows(
                ItemNameAlreadyExistedException.class,
                () -> drinkService.save(expected)
        );
        Assertions.assertEquals("ITEM WITH NAME " + expected.getName() + " HAS ALREADY EXISTED", result.getMessage());
    }

    @Test
    void testFindById_found() {
        Mockito.when(drinkRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Drink actual = drinkService.findById(expected.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getImg(), actual.getImg());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertEquals(expected.getVolume(), actual.getVolume());
    }

    @Test
    void testFindById_notFound() {
        NoItemFoundException result = Assertions.assertThrows(NoItemFoundException.class, () -> drinkService.findById(999));
        Assertions.assertEquals("NO ITEM FOUND WITH ID: 999", result.getMessage());
    }

    @Test
    void testDeleteById_success() {
        Mockito.when(drinkRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        drinkService.deleteById(expected.getId());
        Mockito.verify(drinkRepository).deleteById(expected.getId());
    }

    @Test
    void testGetDrinkMenu() {
        drinkService.getDrinkMenu();
        Mockito.verify(drinkRepository).findAll();
    }

    @Test
    void testUpdate_success() {
        Mockito.when(drinkRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Drink updatedExpected = expected;
        updatedExpected.setDescription("New Description");
        updatedExpected.setPrice(100);

        drinkService.update(updatedExpected);
        Mockito.verify(drinkRepository).save(updatedExpected);
        Mockito.when(drinkRepository.findById(expected.getId())).thenReturn(Optional.of(updatedExpected));

        Drink actual = drinkService.findById(updatedExpected.getId());
        Assertions.assertEquals(updatedExpected.getName(), actual.getName());
        Assertions.assertEquals(updatedExpected.getDescription(), actual.getDescription());
        Assertions.assertEquals(updatedExpected.getImg(), actual.getImg());
        Assertions.assertEquals(updatedExpected.getPrice(), actual.getPrice());
        Assertions.assertEquals(updatedExpected.getVolume(), actual.getVolume());
    }

    @Test
    void testUpdate_updateItemName_conflictWithOtherName() {
        Drink updatedExpected = new SoftDrink(expected);
        updatedExpected.setName("Other Drink");
        updatedExpected.setDescription("New Description");
        updatedExpected.setPrice(100);

        Mockito.when(drinkRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Mockito.when(drinkRepository.findDrinkByName(updatedExpected.getName())).thenReturn(Optional.of(updatedExpected));
        ItemNameAlreadyExistedException result = Assertions.assertThrows(
                ItemNameAlreadyExistedException.class,
                () -> drinkService.update(updatedExpected)
        );

        Assertions.assertEquals("ITEM WITH NAME " + updatedExpected.getName() + " HAS ALREADY EXISTED", result.getMessage());
    }

    @Test
    void testUpdate_updateItemName_success() {
        Drink updatedExpected = new SoftDrink(expected);
        updatedExpected.setName("Update New Name");
        updatedExpected.setDescription("New Description");
        updatedExpected.setPrice(100);

        Mockito.when(drinkRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        drinkService.update(updatedExpected);

        Mockito.verify(drinkRepository).save(updatedExpected);
        Mockito.when(drinkRepository.findById(expected.getId())).thenReturn(Optional.of(updatedExpected));

        Drink actual = drinkService.findById(updatedExpected.getId());
        Assertions.assertEquals(updatedExpected.getName(), actual.getName());
        Assertions.assertEquals(updatedExpected.getDescription(), actual.getDescription());
        Assertions.assertEquals(updatedExpected.getImg(), actual.getImg());
        Assertions.assertEquals(updatedExpected.getPrice(), actual.getPrice());
        Assertions.assertEquals(updatedExpected.getVolume(), actual.getVolume());
    }
}
