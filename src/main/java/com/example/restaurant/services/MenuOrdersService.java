package com.example.restaurant.services;

import com.example.restaurant.DTO.MenuItemIdDTO;
import com.example.restaurant.DTO.MenuOrderPostDTO;
import com.example.restaurant.models.MenuItem;
import com.example.restaurant.models.MenuOrder;
import com.example.restaurant.repositories.MenuItemRepository;
import com.example.restaurant.repositories.MenuOrderRepository;
import com.example.restaurant.repositories.PeopleRepository;
import com.example.restaurant.util.MenuOrderNotCreatedException;
import com.example.restaurant.util.MenuOrderNotFoundException;
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
    private final PeopleRepository peopleRepository;

    @Autowired
    public MenuOrdersService(MenuOrderRepository menuOrderRepository, MenuItemRepository menuItemRepository, PeopleRepository peopleRepository) {
        this.menuOrderRepository = menuOrderRepository;
        this.menuItemRepository = menuItemRepository;
        this.peopleRepository = peopleRepository;
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
    public void save(MenuOrderPostDTO menuOrderPostDTO) {
        MenuOrder menuOrder = createMenuOrder(menuOrderPostDTO);
        menuOrderRepository.save(menuOrder);
    }

    @Transactional
    public void delete(int id) {
        this.findOne(id); // Throws MenuOrderNotFoundException if not found
        menuOrderRepository.deleteById(id);
    }

    public MenuOrder createMenuOrder(MenuOrderPostDTO menuOrderPostDTO) {
        MenuOrder menuOrder = new MenuOrder();
        menuOrder.setCustomer(peopleRepository.findById(menuOrderPostDTO.getPersonId()).orElseThrow(
                () -> new MenuOrderNotCreatedException("Person with id " +
                        menuOrderPostDTO.getPersonId() + " doesn't exist")
        ));
        menuOrder.setOrderedAt(LocalDateTime.now());

        List<MenuItem> menuItems = new ArrayList<>();

        for (MenuItemIdDTO menuItemIdDTO: menuOrderPostDTO.getMenuItemsId()) {
            menuItems.add(menuItemRepository.findById(menuItemIdDTO.getId()).orElseThrow(
                    () -> new MenuOrderNotCreatedException("Menu Item with id " +
                            menuItemIdDTO.getId() + " doesn't exist")));
        }

        menuOrder.setMenuItems(menuItems);

        return menuOrder;
    }

}


