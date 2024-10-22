package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.DrinkDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.ItemNameAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.drink.SoftDrink;
import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.repository.DrinkRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

@Slf4j
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
    void save_success() {
        drinkService.save(expected);
        Mockito.verify(drinkRepository).save(expected);
    }

    @Test
    void save_itemNameHasAlreadyExisted() {
        Mockito.when(drinkRepository.findDrinkByName(expected.getName())).thenReturn(Optional.of(expected));
        ItemNameAlreadyExistedException result = Assertions.assertThrows(
                ItemNameAlreadyExistedException.class,
                () -> drinkService.save(expected)
        );
        Assertions.assertEquals("ITEM WITH NAME " + expected.getName() + " HAS ALREADY EXISTED", result.getMessage());
    }

    @Test
    void findById_found() {
        Mockito.when(drinkRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Drink actual = drinkService.findById(expected.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getImg(), actual.getImg());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
        Assertions.assertEquals(expected.getVolume(), actual.getVolume());
    }

    @Test
    void findById_notFound() {
        NoItemFoundException result = Assertions.assertThrows(NoItemFoundException.class, () -> drinkService.findById(999));
        Assertions.assertEquals("NO ITEM FOUND WITH ID: 999", result.getMessage());
    }

    @Test
    void deleteById_success() {
        Mockito.when(drinkRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        drinkService.deleteById(expected.getId());
        Mockito.verify(drinkRepository).save(expected);
        Assertions.assertEquals(0, expected.getState());
    }

    @Test
    void getDrinkMenu() {
        int page = 1; int perPage = 10;

        Pageable pageable = PageRequest.of(0, perPage);
        Page<Drink> expectedDrinkPage = new PageImpl<>(List.of(expected), pageable, 1);
        Mockito.when(drinkRepository.getListOfActiveDrinks(pageable)).thenReturn(expectedDrinkPage);

        CollectionResponse<DrinkDTO> actual = drinkService.getDrinkMenu(page, perPage);
        Mockito.verify(drinkRepository).getListOfActiveDrinks(PageRequest.of(0, perPage));

        Assertions.assertEquals(expectedDrinkPage.getNumber(), actual.getPage() - 1);
        Assertions.assertEquals(expectedDrinkPage.getSize(), actual.getPerPage());
        Assertions.assertEquals(expectedDrinkPage.getTotalElements(), actual.getTotalItems());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test Drink", "Update New Name"})
    void update_success(String name) {
        Drink updatedExpected = new SoftDrink(expected);
        updatedExpected.setName(name);
        updatedExpected.setDescription("New Description");
        updatedExpected.setPrice(100);

        Mockito.when(drinkRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        drinkService.update(updatedExpected);

        Mockito.verify(drinkRepository).save(updatedExpected);
        Mockito.when(drinkRepository.findById(updatedExpected.getId())).thenReturn(Optional.of(updatedExpected));

        Drink actual = drinkService.findById(updatedExpected.getId());
        Assertions.assertEquals(updatedExpected.getName(), actual.getName());
        Assertions.assertEquals(updatedExpected.getDescription(), actual.getDescription());
        Assertions.assertEquals(updatedExpected.getImg(), actual.getImg());
        Assertions.assertEquals(updatedExpected.getPrice(), actual.getPrice());
        Assertions.assertEquals(updatedExpected.getVolume(), actual.getVolume());
    }

    @Test
    void update_updateItemName_conflictWithOtherName() {
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
}
