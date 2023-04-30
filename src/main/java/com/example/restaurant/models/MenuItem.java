package com.example.restaurant.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Entity
@Table(name = "menu_item")
public class MenuItem {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "title")
    @NotEmpty
    private String title;

    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "menuItems")
    private List<MenuOrder> menuOrders;

    public MenuItem() {
    }

    public MenuItem(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public List<MenuOrder> getMenuOrders() {
        return menuOrders;
    }

    public void setMenuOrders(List<MenuOrder> menuOrders) {
        this.menuOrders = menuOrders;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
