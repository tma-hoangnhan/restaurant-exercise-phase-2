package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.ItemNameAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.Item;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.drink.SoftDrink;
import com.tma.restaurant_exercise_phase_2.repository.ItemRepository;
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

class ItemServiceTest {
    private ItemService itemService;
    @Mock private ItemRepository itemRepository;

    Item expected;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        itemService = new ItemService(itemRepository);

        expected = new SoftDrink("Test Drink", "Test Drink", "Image URL for Test Drink", 10, 500);
        expected.setId(1);
    }

    @Test
    void save_success() {
        itemService.save(expected);
        Mockito.verify(itemRepository).save(expected);
    }

    @Test
    void save_itemNameHasAlreadyExisted() {
        // given
        Mockito.when(itemRepository.findItemByName(expected.getName())).thenReturn(Optional.of(expected));

        // when
        ItemNameAlreadyExistedException result = Assertions.assertThrows(
                ItemNameAlreadyExistedException.class,
                () -> itemService.save(expected)
        );

        // then
        Assertions.assertEquals("ITEM WITH NAME " + expected.getName() + " HAS ALREADY EXISTED", result.getMessage());
    }

    @Test
    void findById_found() {
        // given
        Mockito.when(itemRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        // when
        Item actual = itemService.findById(expected.getId());

        // then
        Assertions.assertEquals(expected.getName(), actual.getName());
        Assertions.assertEquals(expected.getDescription(), actual.getDescription());
        Assertions.assertEquals(expected.getImg(), actual.getImg());
        Assertions.assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    void findById_notFound() {
        // when
        NoItemFoundException result = Assertions.assertThrows(NoItemFoundException.class, () -> itemService.findById(999));

        // then
        Assertions.assertEquals("NO ITEM FOUND WITH ID: 999", result.getMessage());
    }

    @Test
    void deleteById_success() {
        // given
        Mockito.when(itemRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        // when
        itemService.deleteById(expected.getId());

        // then
        Mockito.verify(itemRepository).save(expected);
        Assertions.assertEquals(0, expected.getState());
    }

    @Test
    void getAllItems() {
        // given
        int page = 1; int perPage = 10;

        Pageable pageable = PageRequest.of(0, perPage);
        Page<Item> expectedItemPage = new PageImpl<>(List.of(expected), pageable, 1);
        Mockito.when(itemRepository.getMenu(pageable)).thenReturn(expectedItemPage);

        // when
        CollectionResponse<ItemDTO> actual = itemService.getAllItems(page, perPage);

        // then
        Mockito.verify(itemRepository).getMenu(PageRequest.of(0, perPage));
        Assertions.assertEquals(expectedItemPage.getNumber(), actual.getPage() - 1);
        Assertions.assertEquals(expectedItemPage.getSize(), actual.getPerPage());
        Assertions.assertEquals(expectedItemPage.getTotalElements(), actual.getTotalItems());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test Drink", "Update New Name"})
    void update_success(String name) {
        // given
        Drink updatedExpected = new SoftDrink((Drink) expected);
        updatedExpected.setName(name);
        updatedExpected.setDescription("New Description");
        updatedExpected.setPrice(100);

        // when
        Mockito.when(itemRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        itemService.update(updatedExpected);

        // then
        Mockito.verify(itemRepository).save(updatedExpected);
    }

    @Test
    void update_updateItemName_conflictWithOtherName() {
        // given
        Drink updatedExpected = new SoftDrink((Drink) expected);
        updatedExpected.setName("Other Drink");
        updatedExpected.setDescription("New Description");
        updatedExpected.setPrice(100);

        // when
        Mockito.when(itemRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        Mockito.when(itemRepository.findItemByName(updatedExpected.getName())).thenReturn(Optional.of(updatedExpected));

        ItemNameAlreadyExistedException result = Assertions.assertThrows(
                ItemNameAlreadyExistedException.class,
                () -> itemService.update(updatedExpected)
        );

        // then
        Assertions.assertEquals("ITEM WITH NAME " + updatedExpected.getName() + " HAS ALREADY EXISTED", result.getMessage());
    }
}
