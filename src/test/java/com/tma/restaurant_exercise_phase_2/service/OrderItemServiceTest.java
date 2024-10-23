package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.OrderItemDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.bill.Bill;
import com.tma.restaurant_exercise_phase_2.model.bill.OrderItem;
import com.tma.restaurant_exercise_phase_2.model.food.Breakfast;
import com.tma.restaurant_exercise_phase_2.repository.OrderItemRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class OrderItemServiceTest {
    private OrderItemService orderItemService;

    @Mock private OrderItemRepository orderItemRepository;
    OrderItem expected;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderItemService = new OrderItemService(orderItemRepository);

        expected = new OrderItem();
        expected.setId(1);
        expected.setBill(new Bill(1));
        expected.setQuantity(1);
        expected.setItem(new Breakfast("Food", "Food", "Food", 30));
    }

    @Test
    void save() {
        orderItemService.save(expected);
        Mockito.verify(orderItemRepository).save(expected);
    }

    @Test
    void findById_found() {
        // given
        Mockito.when(orderItemRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        OrderItem actual = orderItemService.findById(1);

        // then
        Mockito.verify(orderItemRepository).findById(1);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getItem().getId(), actual.getItem().getId());
        Assertions.assertEquals(expected.getBill().getId(), actual.getBill().getId());
        Assertions.assertEquals(expected.getQuantity(), actual.getQuantity());
    }

    @Test
    void findById_throwNoItemFoundException() {
        // when
        NoItemFoundException result = Assertions.assertThrows(
                NoItemFoundException.class,
                () -> orderItemService.findById(1000)
        );

        // then
        Assertions.assertEquals("NO ORDER ITEM FOUND WITH ID: 1000", result.getMessage());
    }

    @Test
    void updateOrderItem_quantityGreaterOrEqualsTo1() {
        // given
        OrderItemDTO reqOrderItem = expected.toDTO();
        reqOrderItem.setQuantity(5);

        Mockito.when(orderItemRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        String result = orderItemService.updateOrderItem(reqOrderItem);

        // then
        Mockito.verify(orderItemRepository).save(expected);
        Assertions.assertEquals("Order Item with ID: 1 updated", result);
        Assertions.assertEquals(5, expected.getQuantity());
    }

    @Test
    void updateOrderItem_quantityLessThan1() {
        // given
        OrderItemDTO reqOrderItem = expected.toDTO();
        reqOrderItem.setQuantity(0);

        Mockito.when(orderItemRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        String result = orderItemService.updateOrderItem(reqOrderItem);

        // then
        Mockito.verify(orderItemRepository).deleteById(1);
        Assertions.assertEquals("Order Item with ID: 1 deleted", result);
    }

    @Test
    void deleteById() {
        // given
        Mockito.when(orderItemRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        String result = orderItemService.deleteById(1);

        // then
        Mockito.verify(orderItemRepository).deleteById(1);
        Assertions.assertEquals("Order Item with ID: 1 deleted", result);
    }

}
