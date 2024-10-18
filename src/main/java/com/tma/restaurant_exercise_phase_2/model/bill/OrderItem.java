package com.tma.restaurant_exercise_phase_2.model.bill;

import com.tma.restaurant_exercise_phase_2.model.Item;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
