package com.tma.restaurant_exercise_phase_2.controller;

import com.tma.restaurant_exercise_phase_2.factory.FactoryProvider;
import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.model.Item;
import com.tma.restaurant_exercise_phase_2.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;

@RestController
@RequestMapping(path = "/item")
public class ItemController {
    private final ItemService itemService;

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public CollectionResponse<ItemDTO> getAllItems(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int perPage) {
        if (page < 1 || perPage < 1) throw new InvalidParameterException("page AND perPage MUST BE LARGER THAN 0");
        return itemService.getAllItems(page, perPage);
    }

    @GetMapping(path = "/drink-menu")
    @ResponseStatus(HttpStatus.OK)
    public CollectionResponse<ItemDTO> getDrinkMenu(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int perPage) {
        if (page < 1 || perPage < 1) throw new InvalidParameterException("page AND perPage MUST BE LARGER THAN 0");
        return itemService.getDrinkMenu(page, perPage);
    }

    @GetMapping(path = "/food-menu")
    @ResponseStatus(HttpStatus.OK)
    public CollectionResponse<ItemDTO> getFoodMenu(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "20") int perPage) {
        if (page < 1 || perPage < 1) throw new InvalidParameterException("page AND perPage MUST BE LARGER THAN 0");
        return itemService.getFoodMenu(page, perPage);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createItem(@RequestBody ItemDTO itemDTO) {
        FactoryProvider factoryProvider = FactoryProvider.getInstance();
        String type = itemDTO.getType();
        Item item;

        if (itemDTO.isDrink()) item = factoryProvider.getDrinkFactory(type).createDrink(itemDTO);
        else item = factoryProvider.getFoodFactory(type).createFood(itemDTO);

        itemService.save(item);
        return "Created";
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public String updateItem(@RequestBody ItemDTO itemDTO) {
        FactoryProvider factoryProvider = FactoryProvider.getInstance();
        String type = itemDTO.getType();
        Item item;

        if (itemDTO.isDrink()) item = factoryProvider.getDrinkFactory(type).createDrink(itemDTO);
        else item = factoryProvider.getFoodFactory(type).createFood(itemDTO);
        item.setState(itemDTO.getState());

        itemService.update(item);
        return "Updated";
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public String deleteItemById(@RequestParam int id) {
        itemService.deleteById(id);
        return "Deleted";
    }

    @GetMapping(path = "/details/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemDTO getItemById(@PathVariable int id) {
        return itemService.findById(id).toDTO();
    }

}
