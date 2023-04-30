package com.example.restaurant.DTO;

import java.time.LocalDateTime;
import java.util.List;

public class MenuOrderGetDTO {

    private int id;

    private  int personId;

    private LocalDateTime orderedAt;
    private List<MenuItemGetDTO> menuItems;

    public List<MenuItemGetDTO> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemGetDTO> menuItems) {
        this.menuItems = menuItems;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(LocalDateTime orderedAt) {
        this.orderedAt = orderedAt;
    }
}
