package com.example.restaurant.util;

import com.example.restaurant.DTO.MenuItemGetDTO;
import com.example.restaurant.DTO.MenuOrderGetDTO;
import com.example.restaurant.models.MenuItem;
import com.example.restaurant.models.MenuOrder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConverterToDTO {

    public ConverterToDTO() {

    }

    public List<MenuOrderGetDTO> convertToMenuOrdersGetDTO(List<MenuOrder> menuOrders) {
        List<MenuOrderGetDTO> menuOrderGetDTOS = new ArrayList<>();
        for (MenuOrder menuOrder : menuOrders) {
            menuOrderGetDTOS.add(convertToMenuOrderGetDTO(menuOrder));
        }
        return menuOrderGetDTOS;
    }

    public MenuOrderGetDTO convertToMenuOrderGetDTO(MenuOrder menuOrder) {
        MenuOrderGetDTO menuOrderGetDTO = new MenuOrderGetDTO();
        menuOrderGetDTO.setId(menuOrder.getId());
        menuOrderGetDTO.setOrderedAt(menuOrder.getOrderedAt());
        menuOrderGetDTO.setPersonId(menuOrder.getCustomer().getId());

        List<MenuItemGetDTO> menuItemsGetDTO = new ArrayList<>();
        MenuItemGetDTO menuItemGetDTO;
        for(MenuItem menuItem: menuOrder.getMenuItems()) {
            menuItemGetDTO = convertToMenuItemGetDTO(menuItem);
            menuItemsGetDTO.add(menuItemGetDTO);
        }

        menuOrderGetDTO.setMenuItems(menuItemsGetDTO);

        return menuOrderGetDTO;
    }

    public MenuItemGetDTO convertToMenuItemGetDTO(MenuItem menuItem) {
        MenuItemGetDTO menuItemGetDTO = new MenuItemGetDTO();
        menuItemGetDTO.setDescription(menuItem.getDescription());
        menuItemGetDTO.setTitle(menuItem.getTitle());
        menuItemGetDTO.setId(menuItem.getId());
        return menuItemGetDTO;
    }

}
