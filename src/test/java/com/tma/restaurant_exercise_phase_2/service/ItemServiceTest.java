package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.ItemAlreadyDeletedException;
import com.tma.restaurant_exercise_phase_2.exceptions.ItemNameAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.Item;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.drink.SoftDrink;
import com.tma.restaurant_exercise_phase_2.repository.ItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ItemServiceTest {
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;

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
        verify(itemRepository).save(expected);
    }

    @Test
    void save_itemNameHasAlreadyExisted() {
        // given
        when(itemRepository.findItemByName(expected.getName())).thenReturn(Optional.of(expected));

        // when
        ItemNameAlreadyExistedException result = assertThrows(
                ItemNameAlreadyExistedException.class,
                () -> itemService.save(expected)
        );

        // then
        assertEquals("ITEM WITH NAME " + expected.getName() + " HAS ALREADY EXISTED", result.getMessage());
    }

    @Test
    void findById_found() {
        // given
        when(itemRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        // when
        Item actual = itemService.findById(expected.getId());

        // then
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getImg(), actual.getImg());
        assertEquals(expected.getPrice(), actual.getPrice());
    }

    @Test
    void findById_notFound() {
        // when
        NoItemFoundException result = assertThrows(NoItemFoundException.class, () -> itemService.findById(999));

        // then
        assertEquals("NO ITEM FOUND WITH ID: 999", result.getMessage());
    }

    @Test
    void deleteById_success() {
        // given
        when(itemRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        // when
        itemService.deleteById(expected.getId());

        // then
        verify(itemRepository).save(expected);
        assertEquals(0, expected.getState());
    }

    @Test
    void deleteById_throwItemAlreadyDeletedException() {
        // given
        expected.setState(0);
        when(itemRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        // when
        ItemAlreadyDeletedException result = assertThrows(
                ItemAlreadyDeletedException.class,
                () -> itemService.deleteById(expected.getId())
        );

        // then
        assertEquals("ITEM WITH ID: 1 HAS ALREADY BEEN DELETED", result.getMessage());
    }

    @Test
    void getAllItems() {
        // given
        int page = 1; int perPage = 10;

        Pageable pageable = PageRequest.of(0, perPage);
        Page<Item> expectedItemPage = new PageImpl<>(List.of(expected), pageable, 1);
        when(itemRepository.getMenu(pageable)).thenReturn(expectedItemPage);

        // when
        CollectionResponse<ItemDTO> actual = itemService.getAllItems(page, perPage);

        // then
        verify(itemRepository).getMenu(PageRequest.of(0, perPage));
        assertEquals(expectedItemPage.getNumber(), actual.getPage() - 1);
        assertEquals(expectedItemPage.getSize(), actual.getPerPage());
        assertEquals(expectedItemPage.getTotalElements(), actual.getTotalItems());
    }

    @Test
    void getDrinkMenu() {
        // given
        int page = 1; int perPage = 10;

        Pageable pageable = PageRequest.of(0, perPage);
        Page<Item> expectedItemPage = new PageImpl<>(List.of(expected), pageable, 1);
        when(itemRepository.getListOfActiveDrinks(pageable)).thenReturn(expectedItemPage);

        // when
        CollectionResponse<ItemDTO> actual = itemService.getDrinkMenu(page, perPage);

        // then
        verify(itemRepository).getListOfActiveDrinks(PageRequest.of(0, perPage));
        assertEquals(expectedItemPage.getNumber(), actual.getPage() - 1);
        assertEquals(expectedItemPage.getSize(), actual.getPerPage());
        assertEquals(expectedItemPage.getTotalElements(), actual.getTotalItems());
    }

    @Test
    void getFoodMenu() {
        // given
        int page = 1; int perPage = 10;

        Pageable pageable = PageRequest.of(0, perPage);
        Page<Item> expectedItemPage = new PageImpl<>(List.of(expected), pageable, 1);
        when(itemRepository.getListOfActiveFoods(pageable)).thenReturn(expectedItemPage);

        // when
        CollectionResponse<ItemDTO> actual = itemService.getFoodMenu(page, perPage);

        // then
        verify(itemRepository).getListOfActiveFoods(PageRequest.of(0, perPage));
        assertEquals(expectedItemPage.getNumber(), actual.getPage() - 1);
        assertEquals(expectedItemPage.getSize(), actual.getPerPage());
        assertEquals(expectedItemPage.getTotalElements(), actual.getTotalItems());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test Drink", "Update New Name"})
    void update_success(String name) {
        // given
        Drink updatedExpected = new SoftDrink((Drink) expected);
        updatedExpected.setName(name);
        updatedExpected.setDescription("New Description");
        updatedExpected.setPrice(100);
        when(itemRepository.findById(expected.getId())).thenReturn(Optional.of(expected));

        // when
        itemService.update(updatedExpected);

        // then
        verify(itemRepository).save(updatedExpected);
    }

    @Test
    void update_updateItemName_conflictWithOtherName() {
        // given
        Drink updatedExpected = new SoftDrink((Drink) expected);
        updatedExpected.setName("Other Drink");
        updatedExpected.setDescription("New Description");
        updatedExpected.setPrice(100);
        when(itemRepository.findById(expected.getId())).thenReturn(Optional.of(expected));
        when(itemRepository.findItemByName(updatedExpected.getName())).thenReturn(Optional.of(updatedExpected));

        // when
        ItemNameAlreadyExistedException result = assertThrows(
                ItemNameAlreadyExistedException.class,
                () -> itemService.update(updatedExpected)
        );

        // then
        assertEquals("ITEM WITH NAME " + updatedExpected.getName() + " HAS ALREADY EXISTED", result.getMessage());
    }
}
