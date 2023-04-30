package com.example.restaurant.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "menu_order")
public class MenuOrder {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ordered_at")
    private LocalDateTime orderedAt;

    @ManyToMany
    @JoinTable(
            name = "menu_order__menu_item",
            joinColumns = @JoinColumn(name = "menu_order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<MenuItem> menuItems;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person customer;

    public MenuOrder() {
    }

    public MenuOrder(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

    public Person getCustomer() {
        return customer;
    }

    public void setCustomer(Person customer) {
        this.customer = customer;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }

    @Override
    public String toString() {
        return "MenuOrder{" +
                "id=" + id +
                ", timestamp=" + orderedAt +
                '}';
    }
}
