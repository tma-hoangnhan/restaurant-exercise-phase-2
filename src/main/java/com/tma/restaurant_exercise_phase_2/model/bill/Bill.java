package com.tma.restaurant_exercise_phase_2.model.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @OneToMany(mappedBy = "bill", fetch = FetchType.LAZY)
    private List<OrderItem> orderItemList;

    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;
}
