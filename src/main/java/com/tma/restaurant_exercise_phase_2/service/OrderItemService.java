package com.tma.restaurant_exercise_phase_2.service;

import com.tma.restaurant_exercise_phase_2.dtos.OrderItemDTO;
import com.tma.restaurant_exercise_phase_2.exceptions.NoItemFoundException;
import com.tma.restaurant_exercise_phase_2.model.bill.OrderItem;
import com.tma.restaurant_exercise_phase_2.repository.OrderItemRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;

    @Autowired
    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    public OrderItem findById(int id) {
        return orderItemRepository.findById(id).orElseThrow(() -> new NoItemFoundException("NO ORDER ITEM FOUND WITH ID: " + id));
    }

    public String updateOrderItem(OrderItemDTO orderItemDTO) {
        int updatedQuantity = orderItemDTO.getQuantity();
        if (updatedQuantity < 1) return deleteById(orderItemDTO.getId());

        OrderItem dbOrderItem = findById(orderItemDTO.getId());
        dbOrderItem.setQuantity(updatedQuantity);
        orderItemRepository.save(dbOrderItem);

        return "Order Item with ID: " + orderItemDTO.getId() + " updated";
    }

    public String deleteById(int id) {
        findById(id);
        orderItemRepository.deleteById(id);
        return "Order Item with ID: " + id + " deleted";
    }
}
