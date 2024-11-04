package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.dtos.BillDTO;
import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.dtos.OrderItemDTO;
import com.tma.restaurant_exercise_phase_2.model.bill.Bill;
import com.tma.restaurant_exercise_phase_2.model.bill.OrderItem;
import com.tma.restaurant_exercise_phase_2.model.food.Breakfast;
import com.tma.restaurant_exercise_phase_2.security.services.JwtService;
import com.tma.restaurant_exercise_phase_2.service.BillService;
import com.tma.restaurant_exercise_phase_2.service.OrderItemService;
import com.tma.restaurant_exercise_phase_2.utils.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BillController.class)
@AutoConfigureMockMvc(addFilters = false)
class BillControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean private BillService billService;
    @MockBean private OrderItemService orderItemService;
    @MockBean private JwtService jwtService;

    Bill expectedBill;
    OrderItem expectedOrderItem;

    @BeforeEach
    void setUp() {
        expectedOrderItem = new OrderItem(
                1,
                new Bill(1),
                new Breakfast("Food", "Food", "Food", 30),
                1
        );
        expectedBill = new Bill(1, List.of(expectedOrderItem), LocalDateTime.now());
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void getAllBills_return200() throws Exception {
        int page = 1, perPage = 10;
        CollectionResponse<BillDTO> response = CollectionResponse.<BillDTO>builder()
                .page(page)
                .perPage(perPage)
                .totalPages(1)
                .totalItems(1)
                .contents(List.of(expectedBill.toDTO()))
                .build();

        Mockito.when(billService.getAllBills(page, perPage)).thenReturn(response);
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bill?page=1&perPage=10").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.page").isNumber())
                .andExpect(jsonPath("$.perPage").isNumber())
                .andExpect(jsonPath("$.totalItems").isNumber())
                .andExpect(jsonPath("$.totalPages").isNumber())
                .andExpect(jsonPath("$.contents").isArray());
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void getAllBills_invalidParameters_pageLessThan1_return400() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bill?page=0&perPage=10").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("page AND perPage MUST BE LARGER THAN 0"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void getAllBills_invalidParameters_perPageLessThan1_return400() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bill?page=1&perPage=0").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("page AND perPage MUST BE LARGER THAN 0"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void createBill_return201() throws Exception {
        Mockito.when(billService.save(Mockito.any(Bill.class))).thenReturn(expectedBill);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/bill").accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("Bill 1 created"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void getBillById_return200() throws Exception {
        Mockito.when(billService.getBillDetailsById(1)).thenReturn(expectedBill.toBillDetailsDTO());
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/bill/details/1").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.orderItemList").isArray());
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void deleteBillById_return200() throws Exception {
        Mockito.when(billService.deleteById(1)).thenReturn("Bill with ID: 1 deleted");
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/bill?id=1").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("Bill with ID: 1 deleted"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void addItemToBill_return200() throws Exception {
        Mockito.when(billService.addItemToBill(Mockito.any(OrderItemDTO.class))).thenReturn("1 Food have been added into Bill 1");
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/bill/order-item")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtils.writeJsonString(expectedOrderItem.toDTO()))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("1 Food have been added into Bill 1"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void updateItem_return200() throws Exception {
        Mockito.when(orderItemService.updateOrderItem(Mockito.any(OrderItemDTO.class))).thenReturn("Order Item with ID: 1 updated");
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/bill/order-item")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtils.writeJsonString(expectedOrderItem.toDTO()))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("Order Item with ID: 1 updated"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void deleteOrderItem_return200() throws Exception {
        Mockito.when(orderItemService.deleteById(1)).thenReturn("Order Item with ID: 1 deleted");
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/bill/order-item?id=1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(JsonUtils.writeJsonString(expectedOrderItem.toDTO()))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("Order Item with ID: 1 deleted"));
    }
}
