package com.example.restaurant.models;

import jakarta.persistence.*;

import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "menu_order")
public class MenuOrder {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ordered_at")
    private Timestamp timestamp;

    @ManyToMany
    @JoinTable(
            name = "menu_order__menu_item",
            joinColumns = @JoinColumn(name = "menu_item_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_order_id")
    )
    private List<MenuItem> menuItems;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person customer;

    public MenuOrder() {
    }

    public MenuOrder(Timestamp timestamp) {
        this.timestamp = timestamp;
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

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "MenuOrder{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                '}';
    }
}
