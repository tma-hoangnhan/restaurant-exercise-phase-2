package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.BillDTO;
import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.dtos.OrderItemDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.CannotAddItemToBillException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.Item;
import com.tma.restaurant_exercise_phase_2.model.bill.Bill;
import com.tma.restaurant_exercise_phase_2.model.bill.OrderItem;
import com.tma.restaurant_exercise_phase_2.model.food.Breakfast;
import com.tma.restaurant_exercise_phase_2.repository.BillRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

class BillServiceTest {
    private BillService billService;

    @Mock private BillRepository billRepository;
    @Mock private OrderItemService orderItemService;
    @Mock private ItemService itemService;

    Bill expected;
    OrderItem orderItem1, orderItem2, orderItem3;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        billService = new BillService(billRepository, orderItemService, itemService);

        expected = new Bill(1, generateOrderItems(), LocalDateTime.now());
    }

    List<OrderItem> generateOrderItems() {
        orderItem1 = new OrderItem();
        orderItem1.setId(1);
        orderItem1.setBill(new Bill(1));
        orderItem1.setQuantity(1);
        orderItem1.setItem(new Breakfast("Food", "Food", "Food", 30));

        orderItem2 = new OrderItem();
        orderItem2.setId(2);
        orderItem2.setBill(new Bill(1));
        orderItem2.setQuantity(2);
        orderItem2.setItem(new Breakfast("Food", "Food", "Food", 30));


        orderItem3 = new OrderItem();
        orderItem3.setId(3);
        orderItem3.setBill(new Bill(1));
        orderItem3.setQuantity(3);
        orderItem3.setItem(new Breakfast("Food", "Food", "Food", 30));


        return List.of(orderItem1, orderItem2, orderItem3);
    }

    @Test
    void getAllBills() {
        // given
        int page = 1, perPage = 10;
        Pageable pageable = PageRequest.of(0, perPage);
        Page<Bill> billPage = new PageImpl<>(List.of(expected), pageable, 1);
        Mockito.when(billRepository.getAllBills(pageable)).thenReturn(billPage);

        // when
        CollectionResponse<BillDTO> actual = billService.getAllBills(page, perPage);

        // then
        Mockito.verify(billRepository).getAllBills(pageable);
        Assertions.assertEquals(page, actual.getPage());
        Assertions.assertEquals(perPage, actual.getPerPage());
        Assertions.assertEquals(1, actual.getTotalPages());
        Assertions.assertEquals(1, actual.getTotalItems());
    }

    @Test
    void createNewBill() {
        // given
        Bill bill = new Bill(LocalDateTime.now());

        // when
        billService.save(bill);

        // then
        Mockito.verify(billRepository).save(bill);
    }

    @Test
    void findById_found() {
        // given
        Mockito.when(billRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        Bill actual = billService.findById(1);

        // then
        Mockito.verify(billRepository).findById(1);
        Assertions.assertEquals(expected.getId(), actual.getId());
        Assertions.assertEquals(expected.getOrderedTime(), actual.getOrderedTime());
        Assertions.assertEquals(expected.getOrderItemList().size(), actual.getOrderItemList().size());
    }

    @Test
    void findById_throwNoItemFoundException() {
        // when
        NoItemFoundException result = Assertions.assertThrows(
                NoItemFoundException.class,
                () -> billService.findById(1000)
        );

        // then
        Assertions.assertEquals("NO BILL FOUND WITH ID: 1000", result.getMessage());
    }

    @Test
    void getBillDetailsByID() {
        // given
        Mockito.when(billRepository.findById(1)).thenReturn(Optional.of(expected));
        BillDTO expectedDTO = expected.toDTO();

        // when
        BillDTO actual = billService.getBillDetailsById(1);

        // then
        Mockito.verify(billRepository).findById(1);
        Assertions.assertEquals(expectedDTO.getId(), actual.getId());
        Assertions.assertEquals(expectedDTO.getTotalPrice(), actual.getTotalPrice());
        Assertions.assertEquals(expectedDTO.getOrderedTime(), actual.getOrderedTime());
    }

    @Test
    void addItemToBill_updateQuantity() {
        // given
        OrderItemDTO reqOrderItem = new OrderItemDTO();
        reqOrderItem.setId(1);
        reqOrderItem.setBillId(1);
        reqOrderItem.setItem(orderItem1.getItem().toDTO());
        reqOrderItem.setQuantity(5);

        Mockito.when(itemService.findById(orderItem1.getItem().getId())).thenReturn(orderItem1.getItem());
        Mockito.when(billRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        String result = billService.addItemToBill(reqOrderItem);

        // then
        Mockito.verify(orderItemService).save(orderItem1);
        Assertions.assertEquals(6, orderItem1.getQuantity());
        Assertions.assertEquals("5 Food have been added into Bill 1", result);
    }

    @Test
    void addItemToBill_createNewOrderItem() {
        // given
        OrderItemDTO reqOrderItem = new OrderItemDTO();
        reqOrderItem.setId(1);
        reqOrderItem.setBillId(1);
        reqOrderItem.setQuantity(5);

        Item newItem = new Breakfast("RandomFood", "Food", "Food", 30);
        newItem.setId(4);
        reqOrderItem.setItem(newItem.toDTO());

        Mockito.when(itemService.findById(4)).thenReturn(newItem);
        Mockito.when(billRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        String result = billService.addItemToBill(reqOrderItem);

        // then
        Assertions.assertEquals("5 RandomFood have been added into Bill 1", result);
    }

    @Test
    void addItemToBill_throwCannotAddItemToBillException() {
        // given
        OrderItemDTO reqOrderItem = new OrderItemDTO();
        reqOrderItem.setId(1);
        reqOrderItem.setBillId(1);
        reqOrderItem.setQuantity(5);

        Item newItem = new Breakfast("RandomFood", "Food", "Food", 30);
        newItem.setId(4);
        newItem.setState(0);
        reqOrderItem.setItem(newItem.toDTO());

        Mockito.when(itemService.findById(4)).thenReturn(newItem);

        // when
        CannotAddItemToBillException result = Assertions.assertThrows(
                CannotAddItemToBillException.class,
                () -> billService.addItemToBill(reqOrderItem)
        );

        // then
        Assertions.assertEquals("ITEM WITH ID: 4 IS NOT AVAILABLE", result.getMessage());
    }

    @Test
    void deleteById() {
        // given
        Mockito.when(billRepository.findById(1)).thenReturn(Optional.of(expected));

        // when
        String result = billService.deleteById(1);

        // then
        Mockito.verify(billRepository).deleteById(1);
        Assertions.assertEquals("Bill with ID: 1 deleted", result);
    }

}
