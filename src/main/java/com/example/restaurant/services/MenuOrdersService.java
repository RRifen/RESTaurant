package com.example.restaurant.services;

import com.example.restaurant.DTO.MenuOrderPostDTO;
import com.example.restaurant.models.MenuItem;
import com.example.restaurant.models.MenuOrder;
import com.example.restaurant.models.Person;
import com.example.restaurant.repositories.MenuItemRepository;
import com.example.restaurant.repositories.MenuOrderRepository;
import com.example.restaurant.util.exceptions.MenuOrderNotCreatedException;
import com.example.restaurant.util.exceptions.MenuOrderNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuOrdersService {
    private final MenuOrderRepository menuOrderRepository;
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuOrdersService(MenuOrderRepository menuOrderRepository, MenuItemRepository menuItemRepository) {
        this.menuOrderRepository = menuOrderRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public MenuOrder findOne(int id) {
        return menuOrderRepository.findById(id).orElseThrow(MenuOrderNotFoundException::new);
    }

    public List<MenuOrder> findAll() {
        return menuOrderRepository.findAll();
    }

    public List<MenuOrder> findByPersonId(int id) {
        return menuOrderRepository.findByCustomer_Id(id);
    }

    @Transactional
    public void save(MenuOrderPostDTO menuOrderPostDTO, Person person) {
        MenuOrder menuOrder = createMenuOrder(menuOrderPostDTO, person);
        menuOrderRepository.save(menuOrder);
    }

    @Transactional
    public void delete(int id) {
        this.findOne(id); // Throws MenuOrderNotFoundException if not found
        menuOrderRepository.deleteById(id);
    }

    public MenuOrder createMenuOrder(MenuOrderPostDTO menuOrderPostDTO, Person person) {
        MenuOrder menuOrder = new MenuOrder();
        menuOrder.setCustomer(person);
        menuOrder.setOrderedAt(LocalDateTime.now());

        List<MenuItem> menuItems = new ArrayList<>();

        for (Integer id: menuOrderPostDTO.getMenuItemsId()) {
            menuItems.add(menuItemRepository.findById(id).orElseThrow(
                    () -> new MenuOrderNotCreatedException("Menu Item with id " +
                            id + " doesn't exist")));
        }

        menuOrder.setMenuItems(menuItems);

        return menuOrder;
    }

}


