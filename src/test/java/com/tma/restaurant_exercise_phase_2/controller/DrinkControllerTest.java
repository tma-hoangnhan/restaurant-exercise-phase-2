package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.dtos.DrinkDTO;
import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.drink.SoftDrink;
import com.tma.restaurant_exercise_phase_2.model.reponsebody.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.service.DrinkService;
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

@WebMvcTest(DrinkController.class)
class DrinkControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DrinkService drinkService;

    Drink expected;

    @BeforeEach
    void setUp() {
        expected = new SoftDrink("Test Drink", "Test Drink", "Image URL for Test Drink", 10, 500);
        expected.setId(1);
    }

    @Test
    void getDrinkMenu() throws Exception {
        int page = 1; int perPage = 10;
        CollectionResponse<DrinkDTO> response = new CollectionResponse<>();
        response.setPage(page);
        response.setPerPage(perPage);
        response.setTotalItems(1);
        response.setContents(List.of(expected.toDTO()));

        Mockito.when(drinkService.getDrinkMenu(page, perPage)).thenReturn(response);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/drink?page=1&perPage=10").accept(MediaType.APPLICATION_JSON)
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
    @ValueSource(strings = {"SoftDrink", "Alcohol"})
    void createNewDrink_success(String type) throws Exception {
        DrinkDTO reqDrinkDto = new DrinkDTO();
        reqDrinkDto.setName("Test Drink");
        reqDrinkDto.setDescription("Test Drink");
        reqDrinkDto.setImg("Image URL");
        reqDrinkDto.setPrice(10);
        reqDrinkDto.setVolume(500);
        reqDrinkDto.setType(type);

        String jsonValue = JsonUtils.writeJsonString(reqDrinkDto);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/drink")
                                .content(jsonValue)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("Created"));
    }

    @Test
    void createNewDrink_throwInvalidTypeException() throws Exception {
        String type = "Invalid Type";

        DrinkDTO reqDrinkDto = new DrinkDTO();
        reqDrinkDto.setName("Test Drink");
        reqDrinkDto.setDescription("Test Drink");
        reqDrinkDto.setImg("Image URL");
        reqDrinkDto.setPrice(10);
        reqDrinkDto.setVolume(500);
        reqDrinkDto.setType(type);

        String jsonValue = JsonUtils.writeJsonString(reqDrinkDto);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .post("/drink")
                                .content(jsonValue)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("TYPE " + type + " IS INVALID"));
    }

    @Test
    void getDrinkById_found() throws Exception {
        Mockito.when(drinkService.findById(expected.getId())).thenReturn(expected);

        mockMvc.perform(
                        MockMvcRequestBuilders
                                .get("/drink/" + expected.getId())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isMap())
                .andExpect(jsonPath("$.id").value(expected.getId()));
    }

    @ParameterizedTest
    @ValueSource(strings = {"SoftDrink", "Alcohol"})
    void updateDrink(String type) throws Exception {
        DrinkDTO reqDrinkDto = new DrinkDTO();
        reqDrinkDto.setId(1);
        reqDrinkDto.setName("Test Drink");
        reqDrinkDto.setDescription("Test Drink");
        reqDrinkDto.setImg("Image URL");
        reqDrinkDto.setPrice(10);
        reqDrinkDto.setVolume(500);
        reqDrinkDto.setType(type);

        String jsonValue = JsonUtils.writeJsonString(reqDrinkDto);
        mockMvc.perform(
                        MockMvcRequestBuilders
                                .put("/drink")
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
                                .delete("/drink")
                                .queryParam("id", String.valueOf(expected.getId()))
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isString())
                .andExpect(jsonPath("$").value("Deleted"));
    }
}
