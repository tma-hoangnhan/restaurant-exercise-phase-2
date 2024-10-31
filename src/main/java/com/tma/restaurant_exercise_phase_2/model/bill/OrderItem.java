package com.tma.restaurant_exercise_phase_2.model.bill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tma.restaurant_exercise_phase_2.dtos.OrderItemDTO;
import com.tma.restaurant_exercise_phase_2.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties("bill")
@Table(name = "order_item")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "bill_id")
    private Bill bill;

    @OneToOne
    private Item item;

    @Column(name = "quantity")
    private int quantity;

    public OrderItemDTO toDTO() {
        return OrderItemDTO
                .builder()
                .id(getId())
                .billId(getBill().getId())
                .quantity(getQuantity())
                .item(getItem().toDTO())
                .build();
    }
}
