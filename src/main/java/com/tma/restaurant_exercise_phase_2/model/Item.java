package com.tma.restaurant_exercise_phase_2.model;

import com.tma.restaurant_exercise_phase_2.dtos.ItemDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "item")
public abstract class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
        ItemDTO itemDTO = new ItemDTO();
        itemDTO.setId(getId());
        itemDTO.setName(getName());
        itemDTO.setDescription(getDescription());
        itemDTO.setImg(getImg());
        itemDTO.setPrice(getPrice());
        itemDTO.setType(getType());
        return itemDTO;
    }
}
