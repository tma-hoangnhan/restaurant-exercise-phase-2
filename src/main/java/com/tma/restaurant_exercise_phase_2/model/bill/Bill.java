package com.tma.restaurant_exercise_phase_2.model.bill;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tma.restaurant_exercise_phase_2.dtos.BillDTO;
import com.tma.restaurant_exercise_phase_2.dtos.BillDetailsDTO;
import com.tma.restaurant_exercise_phase_2.dtos.OrderItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties("orderItemList")
@Table(name = "bill")
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @OneToMany(mappedBy = "bill", fetch = FetchType.LAZY, orphanRemoval = true)
    private List<OrderItem> orderItemList;

    @Column(name = "ordered_time")
    private LocalDateTime orderedTime;

    public Bill(int id) {
        this.id = id;
    }

    public Bill(LocalDateTime orderedTime) {
        this.orderedTime = orderedTime;
    }

    public BillDTO toDTO() {
        BillDTO billDTO = new BillDTO();
        billDTO.setId(getId());
        billDTO.setOrderedTime(getOrderedTime());

        double totalPrice = 0;
        for (OrderItem oi : getOrderItemList())
            totalPrice += oi.getQuantity() * oi.getItem().getPrice();

        billDTO.setTotalPrice(totalPrice);
        return billDTO;
    }

    public BillDetailsDTO toBillDetailsDTO() {
        double totalPrice = 0;
        List<OrderItemDTO> orderItemDTOList = new ArrayList<>();
        for (OrderItem oi : getOrderItemList()) {
            totalPrice += oi.getQuantity() * oi.getItem().getPrice();
            orderItemDTOList.add(oi.toDTO());
        }
        return BillDetailsDTO
                .builder()
                .id(getId())
                .orderedTime(getOrderedTime())
                .totalPrice(totalPrice)
                .orderItemList(orderItemDTOList)
                .build();
    }
}
