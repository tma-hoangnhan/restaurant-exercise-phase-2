package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.model.drink.Drink;
import com.tma.restaurant_exercise_phase_2.model.drink.SoftDrink;
import com.tma.restaurant_exercise_phase_2.model.requestbody.RequestDrink;
import com.tma.restaurant_exercise_phase_2.service.DrinkService;
import com.tma.restaurant_exercise_phase_2.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
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
    void testGetDrinkMenu() throws Exception {

        Mockito.when(drinkService.getDrinkMenu()).thenReturn(List.of(expected));

        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/drink").accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andReturn();
        log.info(result.getResponse().getContentAsString());
    }

    @ParameterizedTest
    @ValueSource(strings = {"SoftDrink", "Alcohol"})
    void testCreateNewDrink_typeOfSoftDrink(String type) throws Exception {
        RequestDrink requestDrink = new RequestDrink("Test Drink", "Test Drink", "Image URL for Test Drink", 10, 500, type);
        requestDrink.setId(1);

        String jsonValue = JsonUtils.writeJsonString(requestDrink);
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
    void testCreateNewDrink_throwInvalidTypeException() throws Exception {
        String type = "Invalid Type";
        RequestDrink requestDrink = new RequestDrink("Test Drink", "Test Drink", "Image URL for Test Drink", 10, 500, type);
        requestDrink.setId(1);

        String jsonValue = JsonUtils.writeJsonString(requestDrink);
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
    void testGetDrinkById_found() throws Exception {
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
    void testUpdateDrink(String type) throws Exception {
        RequestDrink requestDrink = new RequestDrink("Test Drink", "Test Drink", "Image URL for Test Drink", 10, 500, type);
        requestDrink.setId(1);

        String jsonValue = JsonUtils.writeJsonString(requestDrink);
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
    void testDeleteDrink() throws Exception {
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
