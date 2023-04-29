package com.example.restaurant.models;

import jakarta.persistence.*;

import java.sql.Timestamp;

@Entity
@Table(name = "menu_order")
public class MenuOrder {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "ordered_at")
    private Timestamp timestamp;

    public MenuOrder() {
    }

    public MenuOrder(Timestamp timestamp) {
        this.timestamp = timestamp;
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
