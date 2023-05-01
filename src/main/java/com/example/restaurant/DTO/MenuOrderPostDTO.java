package com.example.restaurant.DTO;

import java.util.List;

public class MenuOrderPostDTO {
    private  int personId;
    private List<Integer> menuItemsId;

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public List<Integer> getMenuItemsId() {
        return menuItemsId;
    }

    public void setMenuItemsId(List<Integer> menuItemsId) {
        this.menuItemsId = menuItemsId;
    }
}
