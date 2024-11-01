package com.tma.restaurant_exercise_phase_2.model;

import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "item")
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "hibernate_sequence")
    @Column(name = "id")
    protected int id;

    @Column(name = "name")
    protected String name;

    @Column(name = "description")
    protected String description;

    @Column(name = "img")
    protected String img;

    @Column(name = "price")
    protected double price;

    @Column(name = "state")
    protected int state;

    @Column(name = "quantity")
    protected int quantity;
    
    @Transient
    protected String type;

    protected Item(String name, String description, String img, double price) {
        this.name = name;
        this.description = description;
        this.img = img;
        this.price = price;
        this.state = 1;
    }

    protected Item(int id, String name, String description, String img, double price) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.img = img;
        this.price = price;
        this.state = 1;
    }

    public ItemDTO toDTO() {
        return ItemDTO.builder()
                .id(getId())
                .name(getName())
                .description(getDescription())
                .img(getImg())
                .price(getPrice())
                .state(getState())
                .quantity(getQuantity())
                .type(getType())
                .build();
    }

    /**
     * Check state of an Item
     * @return true if the state == 1
     */
    public boolean available() {
        return this.state == 1;
    }
}
