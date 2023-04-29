package com.example.restaurant.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.annotations.JdbcType;
import org.hibernate.type.descriptor.jdbc.VarbinaryJdbcType;

import java.util.Arrays;
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

    @Lob
    @JdbcType(VarbinaryJdbcType.class)
    @Column(name = "image")
    private byte[] image;

    @ManyToMany(mappedBy = "menuItems")
    private List<MenuOrder> menuOrders;

    public MenuItem() {
    }

    public MenuItem(String title, String description, byte[] image) {
        this.title = title;
        this.description = description;
        this.image = image;
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

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", image=" + Arrays.toString(image) +
                '}';
    }
}
