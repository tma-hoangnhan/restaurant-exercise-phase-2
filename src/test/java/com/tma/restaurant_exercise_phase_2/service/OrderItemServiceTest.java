package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.OrderItemDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.CannotAddItemToBillException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.Item;
import com.tma.restaurant_exercise_phase_2.model.bill.Bill;
import com.tma.restaurant_exercise_phase_2.model.bill.OrderItem;
import com.tma.restaurant_exercise_phase_2.model.food.Breakfast;
import com.tma.restaurant_exercise_phase_2.repository.OrderItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderItemServiceTest {
    private OrderItemService orderItemService;

    @Mock private OrderItemRepository orderItemRepository;
    @Mock private ItemService itemService;
    OrderItem expected;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderItemService = new OrderItemService(orderItemRepository, itemService);

        expected = new OrderItem();
        expected.setId(1);
        expected.setBill(new Bill(1));
        expected.setQuantity(1);

        Item item = new Breakfast("Food", "Food", "Food", 30);
        item.setId(1);
        item.setQuantity(5);
        expected.setItem(item);
    }

    @Test
    void save() {
        orderItemService.save(expected);
        verify(orderItemRepository).save(expected);
    }

    @Test
    void findById_found() {
        // given
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        OrderItem actual = orderItemService.findById(1);

        // then
        verify(orderItemRepository).findById(1);
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getItem().getId(), actual.getItem().getId());
        assertEquals(expected.getBill().getId(), actual.getBill().getId());
        assertEquals(expected.getQuantity(), actual.getQuantity());
    }

    @Test
    void findById_throwNoItemFoundException() {
        // when
        NoItemFoundException result = assertThrows(
                NoItemFoundException.class,
                () -> orderItemService.findById(1000)
        );

        // then
        assertEquals("NO ORDER ITEM FOUND WITH ID: 1000", result.getMessage());
    }

    @Test
    void updateOrderItem_quantityGreaterOrEqualsTo1() {
        // given
        Item item = new Breakfast("RandomFood", "Food", "Food", 30);
        item.setId(1);
        item.setQuantity(5);
        when(itemService.findById(1)).thenReturn(item);

        OrderItemDTO reqOrderItem = expected.toDTO();
        reqOrderItem.setItem(item.toDTO());
        reqOrderItem.setQuantity(5);

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        String result = orderItemService.updateOrderItem(reqOrderItem);

        // then
        verify(orderItemRepository).save(expected);
        assertEquals("Order Item with ID: 1 updated", result);
        assertEquals(5, expected.getQuantity());
    }

    @Test
    void updateOrderItem_quantityLessThan1() {
        // given
        OrderItemDTO reqOrderItem = expected.toDTO();
        reqOrderItem.setQuantity(0);

        when(orderItemRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        String result = orderItemService.updateOrderItem(reqOrderItem);

        // then
        verify(orderItemRepository).deleteById(1);
        assertEquals("Order Item with ID: 1 deleted", result);
    }

    @Test
    void updateOrderItem_quantityIsExceeded() {
        // given
        Item item = new Breakfast("RandomFood", "Food", "Food", 30);
        item.setId(1);
        item.setQuantity(5);
        when(itemService.findById(1)).thenReturn(item);

        OrderItemDTO reqOrderItem = expected.toDTO();
        reqOrderItem.setItem(item.toDTO());
        reqOrderItem.setQuantity(10);

        expected.setItem(item);
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        CannotAddItemToBillException result = assertThrows(
                CannotAddItemToBillException.class,
                () -> orderItemService.updateOrderItem(reqOrderItem)
        );
        assertEquals("QUANTITY OF ORDERED RandomFood(10) IS LARGER THAN THE AVAILABLE ONE(6)", result.getMessage());
    }

    @Test
    void deleteById() {
        // given
        when(orderItemRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        String result = orderItemService.deleteById(1);

        // then
        verify(orderItemRepository).deleteById(1);
        assertEquals("Order Item with ID: 1 deleted", result);
    }

}
