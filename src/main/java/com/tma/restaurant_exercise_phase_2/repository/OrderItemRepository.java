package com.tma.restaurant_exercise_phase_2.repository;

import com.tma.restaurant_exercise_phase_2.model.bill.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    @Query("SELECT o FROM OrderItem o  WHERE o.bill.id = ?1")
    List<OrderItem> getListOfOrderItemsByBillId(int billId);
}
