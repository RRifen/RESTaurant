package com.example.restaurant.DTO;

import java.util.List;

public class MenuOrderPostDTO {
    private  int personId;
    private List<MenuItemIdDTO> menuItemsId;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public List<MenuItemIdDTO> getMenuItemsId() {
        return menuItemsId;
    }

    public void setMenuItemsId(List<MenuItemIdDTO> menuItemsId) {
        this.menuItemsId = menuItemsId;
    }
}
