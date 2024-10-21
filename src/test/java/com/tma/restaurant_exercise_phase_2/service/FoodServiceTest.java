package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.FoodDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.ItemNameAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.food.Breakfast;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import com.tma.restaurant_exercise_phase_2.model.reponsebody.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.repository.FoodRepository;
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
    void getFoodMenu() {
        int page = 1; int perPage = 10;
        CollectionResponse<FoodDTO> foodCollectionResponse = new CollectionResponse<>();
        foodCollectionResponse.setPage(page);
        foodCollectionResponse.setPerPage(perPage);
        foodCollectionResponse.setTotalItems(1);
        foodCollectionResponse.setContents(List.of(expected.toDTO()));

        Pageable pageable = PageRequest.of(0, perPage);
        Page<Food> expectedFoodPage = new PageImpl<>(List.of(expected), pageable, 1);

        Mockito.when(foodRepository.getListOfActiveFoods(pageable)).thenReturn(expectedFoodPage);
        CollectionResponse<FoodDTO>  actual = foodService.getFoodMenu(page, perPage);
        Mockito.verify(foodRepository).getListOfActiveFoods(pageable);

        Assertions.assertEquals(expectedFoodPage.getNumber(), actual.getPage() - 1);
        Assertions.assertEquals(expectedFoodPage.getSize(), actual.getPerPage());
        Assertions.assertEquals(expectedFoodPage.getTotalElements(), actual.getTotalItems());
    }

    @Test
    void save_success() {
        foodService.save(expected);
        Mockito.verify(foodRepository).save(expected);
    }

    @Test
    void save_itemNameHasAlreadyExisted() {
        Mockito.when(foodRepository.findFoodByName(expected.getName())).thenReturn(Optional.of(expected));
        ItemNameAlreadyExistedException result = Assertions.assertThrows(
                ItemNameAlreadyExistedException.class,
                () -> foodService.save(expected)
        );
        Assertions.assertEquals("ITEM WITH NAME " + expected.getName() + " HAS ALREADY EXISTED", result.getMessage());
    }

    @Test
    void findById_found() {
        Mockito.when(foodRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        Food actual = foodService.findById(expected.getId());
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getImg(), actual.getImg());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    void findById_notFound() {
        NoItemFoundException result = Assertions.assertThrows(NoItemFoundException.class, () -> foodService.findById(999));
        Assertions.assertEquals("NO ITEM FOUND WITH ID: 999", result.getMessage());
    }

    @Test
    void deleteById_success() {
        Mockito.when(foodRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        foodService.deleteById(expected.getId());
        Mockito.verify(foodRepository).save(expected);
        Assertions.assertEquals(0, expected.getState());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test Breakfast", "New Name Of Food"})
    void update_success(String name) {
        Food updatedExpected = new Breakfast(expected);
        updatedExpected.setName(name);
        updatedExpected.setDescription("New Description");
        updatedExpected.setPrice(100);

        Mockito.when(foodRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        foodService.update(updatedExpected);

        Mockito.verify(foodRepository).save(updatedExpected);
        Mockito.when(foodRepository.findById(updatedExpected.getId())).thenReturn(Optional.of(updatedExpected));

        Food actual = foodService.findById(updatedExpected.getId());
        Assertions.assertEquals(updatedExpected.getName(), actual.getName());
        Assertions.assertEquals(updatedExpected.getDescription(), actual.getDescription());
        Assertions.assertEquals(updatedExpected.getImg(), actual.getImg());
        Assertions.assertEquals(updatedExpected.getPrice(), actual.getPrice());
    }

    @Test
    void update_conflictWithOtherName() {
        Food updatedExpected = new Breakfast(expected);
        updatedExpected.setName("Other Food");
        updatedExpected.setDescription("New Description");
        updatedExpected.setPrice(100);

        Mockito.when(foodRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Mockito.when(foodRepository.findFoodByName(updatedExpected.getName())).thenReturn(Optional.of(updatedExpected));

        ItemNameAlreadyExistedException result = Assertions.assertThrows(
                ItemNameAlreadyExistedException.class,
                () -> foodService.update(updatedExpected)
        );

        Assertions.assertEquals("ITEM WITH NAME " + updatedExpected.getName() + " HAS ALREADY EXISTED", result.getMessage());
    }
}
