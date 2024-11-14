package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.CollectionResponse;
import com.tma.restaurant_exercise_phase_2.dtos.FilterRequest;
import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.ItemAlreadyDeletedException;
import com.tma.restaurant_exercise_phase_2.exceptions.ItemNameAlreadyExistedException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.Item;
import com.tma.restaurant_exercise_phase_2.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    /**
     * Find an Item by its ID
     * @param id ID of an Item
     * @return an Item if the ID is existed
     * @exception NoItemFoundException if the ID is not existed
     */
    public Item findById(int id) {
        return itemRepository.findById(id).orElseThrow(() -> new NoItemFoundException("NO ITEM FOUND WITH ID: " + id));
    }

    private CollectionResponse<ItemDTO> createCollectionResponse(Page<Item> itemPage) {
        return CollectionResponse
                .<ItemDTO>builder()
                .page(itemPage.getNumber() + 1)
                .perPage(itemPage.getSize())
                .totalPages(itemPage.getTotalPages())
                .totalItems(itemPage.getTotalElements())
                .contents(itemPage.stream().map(Item::toDTO).toList())
                .build();
    }

    /**
     * Get all Items existing in the database
     * @param page page number wanted to see
     * @param perPage number of items per one page
     * @return CollectionResponse containing Pagination properties and list of Items
     */
    public CollectionResponse<ItemDTO> getAllItems(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        return createCollectionResponse(itemRepository.getMenu(pageable));
    }

    /**
     * Get all the ACTIVE(state = 1) Drink existing in the database
     * @param page page number wanted to see
     * @param perPage number of items per one page
     * @return CollectionResponse containing Pagination properties and list of Drinks
     */
    public CollectionResponse<ItemDTO> getDrinkMenu(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        return createCollectionResponse(itemRepository.getListOfActiveDrinks(pageable));
    }

    /**
     * Get all the ACTIVE(state = 1) Food existing in the database
     * @param page page number wanted to see
     * @param perPage number of items per one page
     * @return CollectionResponse containing Pagination properties and list of Foods
     */
    public CollectionResponse<ItemDTO> getFoodMenu(int page, int perPage) {
        Pageable pageable = PageRequest.of(page - 1, perPage);
        return createCollectionResponse(itemRepository.getListOfActiveFoods(pageable));
    }

    private Optional<Item> findItemByName(String name) {
        return itemRepository.findItemByName(name);
    }

    /**
     * Save the new Item into database
     * @param item object containing new Item's properties
     * @return the new Item which has just been created
     * @exception ItemNameAlreadyExistedException if new Item's name is conflicted with other existed
     */
    public Item save(Item item) {
        Optional<Item> optionalDBItem = findItemByName(item.getName());
        if (optionalDBItem.isPresent()) throw new ItemNameAlreadyExistedException("ITEM WITH NAME " + item.getName() + " HAS ALREADY EXISTED");
        return itemRepository.save(item);
    }

    /**
     * Update the existing Item with new information
     * @param updatedItem object containing new information
     * @exception NoItemFoundException if the requested ID is not found
     * @exception ItemNameAlreadyExistedException if new updated Item's name is conflicted with other existed
     */
    public void update(Item updatedItem) {
        // Check if the requested ID is existed
        Item dbItem = findById(updatedItem.getId());

        // Check the duplication of the updated item
        if (!updatedItem.getName().equals(dbItem.getName())) {
            Optional<Item> optional = findItemByName(updatedItem.getName());
            if (optional.isPresent()) throw new ItemNameAlreadyExistedException("ITEM WITH NAME " + updatedItem.getName() + " HAS ALREADY EXISTED");
        }
        itemRepository.save(updatedItem);
    }

    /**
     * Safe-delete an Item by setting its state's value into 0
     * @param id ID of an Item
     * @exception NoItemFoundException if the requested ID is not found
     */
    public void deleteById(int id) {
        Item item = findById(id);
        if (item.getState() == 0) throw new ItemAlreadyDeletedException("ITEM WITH ID: " + id + " HAS ALREADY BEEN DELETED");
        item.setState(0);
        itemRepository.save(item);
    }

    private Class<?> findClassByClassName(String className) {
        try {
            return Class.forName("com.tma.restaurant_exercise_phase_2.model.drink." + className);
        } catch (ClassNotFoundException e) {
            try {
                return Class.forName("com.tma.restaurant_exercise_phase_2.model.food." + className);
            } catch (ClassNotFoundException ex) {
                throw new NoItemFoundException("NO CLASS FOUND WITH: " + className);
            }
        }
    }

    public CollectionResponse<ItemDTO> filterItem(FilterRequest request, int page, int perPage) {
        List<Class<?>> classes = request.getItemTypes().stream().map(this::findClassByClassName).collect(Collectors.toList());

        Page<Item> itemPage = itemRepository.filterItem(classes, PageRequest.of(page - 1, perPage));
        return createCollectionResponse(itemPage);
    }

    /**
     * Update the quantity of Item after placing OrderItem
     * @param item Item needed to be updated the number of quantity
     * @param orderedQuantity the quantity of OrderItem
     */
    public void updateItemQuantity(Item item, int orderedQuantity) {
        item.setQuantity(item.getQuantity() - orderedQuantity);
        itemRepository.save(item);
    }
}
