package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.dtos.DrinkDTO;
import com.tma.restaurant_exercise_phase_2.dtos.FilterRequest;
import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.drink.SoftDrink;
import com.tma.restaurant_exercise_phase_2.security.services.JwtService;
import com.tma.restaurant_exercise_phase_2.service.ItemService;
import com.tma.restaurant_exercise_phase_2.utils.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ItemController.class)
@AutoConfigureMockMvc(addFilters = false)
class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean private ItemService itemService;
    @MockBean private JwtService jwtService;

    Drink expected;

    @BeforeEach
    void setUp() {
        expected = new SoftDrink("Test Drink", "Test Drink", "Image URL for Test Drink", 10, 500);
        expected.setId(1);
    }

    @Nested
    @WithMockUser(username = "user@gmail.com")
    class getListOfItems {
        @Test
        void getAllItems_return200() throws Exception {
            int page = 1; int perPage = 10;
            CollectionResponse<ItemDTO> response = CollectionResponse
                    .<ItemDTO>builder()
                    .page(page)
                    .perPage(perPage)
                    .totalPages(1)
                    .totalItems(1)
                    .contents(List.of(expected.toDTO()))
                    .build();
            Mockito.when(itemService.getAllItems(page, perPage)).thenReturn(response);
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/item?page=1&perPage=10").accept(MediaType.APPLICATION_JSON)
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
        void getAllItems_invalidParameter_pageLessThan1_return400() throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/item?page=0&perPage=10").accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("page AND perPage MUST BE LARGER THAN 0"));
        }

        @Test
        void getAllItems_invalidParameter_perPageLessThan1_return400() throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/item?page=1&perPage=0").accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("page AND perPage MUST BE LARGER THAN 0"));
        }
    }

    @Nested
    @WithMockUser(username = "user@gmail.com")
    class getDrinkMenu {
        @Test
        void getDrinkMenu_return200() throws Exception {
            int page = 1; int perPage = 10;
            CollectionResponse<ItemDTO> response = CollectionResponse
                    .<ItemDTO>builder()
                    .page(page)
                    .perPage(perPage)
                    .totalPages(1)
                    .totalItems(1)
                    .contents(List.of(expected.toDTO()))
                    .build();
            Mockito.when(itemService.getDrinkMenu(page, perPage)).thenReturn(response);
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/item/drink-menu?page=1&perPage=10").accept(MediaType.APPLICATION_JSON)
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
        void getDrinkMenu_invalidParameter_return400() throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/item/drink-menu?page=0&perPage=10").accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("page AND perPage MUST BE LARGER THAN 0"));
        }

        @Test
        void getDrinkMenu_invalidParameter_perPageLessThan1_return400() throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/item/drink-menu?page=1&perPage=0").accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("page AND perPage MUST BE LARGER THAN 0"));
        }
    }

    @Nested
    @WithMockUser(username = "user@gmail.com")
    class getFoodMenu {
        @Test
        void getFoodMenu_return200() throws Exception {
            int page = 1; int perPage = 10;
            CollectionResponse<ItemDTO> response = CollectionResponse
                    .<ItemDTO>builder()
                    .page(page)
                    .perPage(perPage)
                    .totalPages(1)
                    .totalItems(1)
                    .contents(List.of(expected.toDTO()))
                    .build();
            Mockito.when(itemService.getFoodMenu(page, perPage)).thenReturn(response);
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/item/food-menu?page=1&perPage=10").accept(MediaType.APPLICATION_JSON)
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
        void getFoodMenu_invalidParameter_return400() throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/item/food-menu?page=0&perPage=10").accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("page AND perPage MUST BE LARGER THAN 0"));
        }

        @Test
        void getFoodMenu_invalidParameter_perPageLessThan1_return400() throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/item/food-menu?page=1&perPage=0").accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("page AND perPage MUST BE LARGER THAN 0"));
        }
    }

    @Nested
    @WithMockUser(username = "user@gmail.com")
    class filterItem {
        FilterRequest request = new FilterRequest(List.of("SoftDrink"));
        @Test
        void filterItem_return200() throws Exception {
            int page = 1; int perPage = 10;
            CollectionResponse<ItemDTO> response = CollectionResponse
                    .<ItemDTO>builder()
                    .page(page)
                    .perPage(perPage)
                    .totalPages(1)
                    .totalItems(1)
                    .contents(List.of(expected.toDTO()))
                    .build();
            Mockito.when(itemService.filterItem(Mockito.any(FilterRequest.class), eq(page), eq(perPage))).thenReturn(response);
            mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post("/item/filter?page=1&perPage=10")
                                    .content(JsonUtils.writeJsonString(request))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
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
        void filterItem_invalidParameter_return400() throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post("/item/filter?page=0&perPage=10")
                                    .content(JsonUtils.writeJsonString(request))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("page AND perPage MUST BE LARGER THAN 0"));
        }

        @Test
        void filterItem_invalidParameter_perPageLessThan1_return400() throws Exception {
            mockMvc.perform(
                            MockMvcRequestBuilders
                                    .post("/item/filter?page=1&perPage=0")
                                    .content(JsonUtils.writeJsonString(request))
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").isString())
                    .andExpect(jsonPath("$").value("page AND perPage MUST BE LARGER THAN 0"));
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser(username = "user@gmail.com")
    void createItem_return201(boolean isDrink) throws Exception {
        String type = isDrink ? "SoftDrink" : "Breakfast";
        ItemDTO reqDrinkDto = ItemDTO
                .builder()
                .name("Test Drink")
                .description("Test Drink")
                .img("Image URL")
                .price(10)
                .volume(500)
                .type(type)
                .isDrink(isDrink)
                .build();
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/item")
                                .content(JsonUtils.writeJsonString(reqDrinkDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("Created"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void createItem_invalidType_return400() throws Exception {
        String type = "Invalid Type";
        DrinkDTO reqDrinkDto = DrinkDTO
                .builder()
                .name("Test Drink")
                .description("Test Drink")
                .img("Image URL")
                .price(10)
                .volume(500)
                .type(type)
                .build();
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/item")
                                .content(JsonUtils.writeJsonString(reqDrinkDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string(type + " IS INVALID"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void getItemById_return200() throws Exception {
        Mockito.when(itemService.findById(expected.getId())).thenReturn(expected);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/item/details/" + expected.getId())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(expected.getId()));
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    @WithMockUser(username = "user@gmail.com")
    void updateItem_return200(boolean isDrink) throws Exception {
        String type = isDrink ? "SoftDrink" : "Breakfast";
        DrinkDTO reqDrinkDto = DrinkDTO
                .builder()
                .name("Test Drink")
                .description("Test Drink")
                .img("Image URL")
                .price(10)
                .volume(500)
                .type(type)
                .isDrink(isDrink)
                .build();
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/item")
                                .content(JsonUtils.writeJsonString(reqDrinkDto))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("Updated"));
    }

    @Test
    @WithMockUser(username = "user@gmail.com")
    void deleteItemById_return200() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/item")
                                .queryParam("id", String.valueOf(expected.getId()))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("Deleted"));
    }
}
