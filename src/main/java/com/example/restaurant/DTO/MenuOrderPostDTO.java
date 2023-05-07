package com.example.restaurant.DTO;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class MenuOrderPostDTO {
    @NotEmpty
    private List<Integer> menuItemsId;

    public List<Integer> getMenuItemsId() {
        return menuItemsId;
    }

    public void setMenuItemsId(List<Integer> menuItemsId) {
        this.menuItemsId = menuItemsId;
    }
}
