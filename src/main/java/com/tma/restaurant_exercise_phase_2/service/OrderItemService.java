package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.OrderItemDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.CannotAddItemToBillException;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.Item;
import com.tma.restaurant_exercise_phase_2.model.bill.OrderItem;
import com.tma.restaurant_exercise_phase_2.repository.OrderItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final ItemService itemService;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository, ItemService itemService) {
        this.orderItemRepository = orderItemRepository;
        this.itemService = itemService;
    }

    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    public OrderItem findById(int id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new NoItemFoundException("NO ORDER ITEM FOUND WITH ID: " + id));
    }

    @Transactional
    public String updateOrderItem(OrderItemDTO orderItemDTO) {
        int updatedQuantity = orderItemDTO.getQuantity();
        if (updatedQuantity < 1) return deleteById(orderItemDTO.getId());

        OrderItem orderItem = findById(orderItemDTO.getId());
        Item item = itemService.findById(orderItem.getItem().getId());

        int totalQuantity = item.getQuantity() + orderItem.getQuantity();
        if (updatedQuantity > totalQuantity)
            throw new CannotAddItemToBillException("QUANTITY OF ORDERED " + item.getName() + "(" + updatedQuantity + ") IS LARGER THAN THE AVAILABLE ONE(" + totalQuantity + ")");

        int quantityBalance = updatedQuantity - orderItem.getQuantity();

        orderItem.setQuantity(updatedQuantity);
        orderItemRepository.save(orderItem);

        itemService.updateItemQuantity(item, quantityBalance);

        return "Order Item with ID: " + orderItemDTO.getId() + " updated";
    }

    @Transactional
    public String deleteById(int id) {
        OrderItem orderItem = findById(id);
        orderItemRepository.deleteById(id);

        Item item = itemService.findById(orderItem.getItem().getId());
        itemService.updateItemQuantity(item, -orderItem.getQuantity());
        return "Order Item with ID: " + id + " deleted";
    }
}
