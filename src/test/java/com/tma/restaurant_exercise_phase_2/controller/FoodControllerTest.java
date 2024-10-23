package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.dtos.FoodDTO;
import com.tma.restaurant_exercise_phase_2.model.food.Breakfast;
import com.tma.restaurant_exercise_phase_2.model.food.Food;
import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.model.requestbody.RequestFood;
import com.tma.restaurant_exercise_phase_2.service.FoodService;
import com.tma.restaurant_exercise_phase_2.utils.JsonUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoodController.class)
class FoodControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FoodService foodService;

    Food expected;

    @BeforeEach
    void setUp() {
        expected = new Breakfast("Noodles", "Noodles", "Noodles", 30);
        expected.setId(1);
    }

    @Test
    void getFoodMenu() throws Exception {
        int page = 1; int perPage = 10;
        CollectionResponse<FoodDTO> foodCollectionResponse = new CollectionResponse<>();
        foodCollectionResponse.setPage(page);
        foodCollectionResponse.setPerPage(perPage);
        foodCollectionResponse.setTotalItems(1);
        foodCollectionResponse.setContents(List.of(expected.toDTO()));

        Mockito.when(foodService.getFoodMenu(page, perPage)).thenReturn(foodCollectionResponse);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/food?page=1&perPage=10").accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.page").isNumber())
                .andExpect(jsonPath("$.perPage").isNumber())
                .andExpect(jsonPath("$.totalItems").isNumber())
                .andExpect(jsonPath("$.totalPages").isNumber())
                .andExpect(jsonPath("$.contents").isArray())
                .andReturn();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Breakfast", "Lunch", "Dinner"})
    void createNewFood_success(String type) throws Exception {
        RequestFood requestFood = new RequestFood("Test Food", "Test Food", "Test Food", 30, type);
        String jsonValue = JsonUtils.writeJsonString(requestFood);

        mockMvc.perform(
                MockMvcRequestBuilders.post("/food")
                        .content(jsonValue)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").value("Created"));
    }

    @Test
    void createNewFood_throwInvalidTypeException() throws Exception {
        String type = "Invalid Type";
        RequestFood requestFood = new RequestFood("Test Food", "Test Food", "Test Food", 30, type);
        String jsonValue = JsonUtils.writeJsonString(requestFood);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/food")
                                .content(jsonValue)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("TYPE " + type + " IS INVALID"));
    }

    @Test
    void getFoodById_found() throws Exception {
        Mockito.when(foodService.findById(expected.getId())).thenReturn(expected);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/food/" + expected.getId())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(expected.getId()));

        Mockito.verify(foodService).findById(expected.getId());
    }

    @ParameterizedTest
    @ValueSource(strings = {"Breakfast", "Lunch", "Dinner"})
    void updateFood(String type) throws Exception {
        FoodDTO foodDTO = new FoodDTO();
        foodDTO.setId(1);
        foodDTO.setName("Test Food");
        foodDTO.setDescription("Test Food");
        foodDTO.setImg("Image URL");
        foodDTO.setPrice(30);
        foodDTO.setType(type);

        String jsonValue = JsonUtils.writeJsonString(foodDTO);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/food")
                                .content(jsonValue)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("Updated"));
    }

    @Test
    void deleteDrink() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .delete("/food")
                                .queryParam("id", String.valueOf(expected.getId()))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("Deleted"));
    }

}
