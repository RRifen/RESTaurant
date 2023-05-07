package com.example.restaurant.services;

import com.example.restaurant.DTO.MenuItemPostDTO;
import com.example.restaurant.models.MenuItem;
import com.example.restaurant.repositories.MenuItemRepository;
import com.example.restaurant.util.exceptions.MenuItemNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuItemsService {
    private final MenuItemRepository menuItemRepository;

    @Autowired
    public MenuItemsService(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    public MenuItem findOne(int id) {
        return menuItemRepository.findById(id).orElseThrow(MenuItemNotFoundException::new);
    }

    @Transactional
    public void save(MenuItemPostDTO menuItemPostDTO) {
        MenuItem menuItem = createMenuItem(menuItemPostDTO);
        menuItemRepository.save(menuItem);
    }

    @Transactional
    public void update(MenuItemPostDTO menuItemPostDTO, int id) {
        MenuItem menuItem = createMenuItem(menuItemPostDTO);
        menuItem.setId(id);
        menuItemRepository.findById(id).orElseThrow(MenuItemNotFoundException::new);
        menuItemRepository.save(menuItem);
    }

    @Transactional
    public void delete(int id) {
        this.findOne(id); // Throws MenuItemNotFoundException if not found
        menuItemRepository.deleteById(id);
    }

    public MenuItem createMenuItem(MenuItemPostDTO menuItemPostDTO) {
        MenuItem menuItem = new MenuItem();
        menuItem.setTitle(menuItemPostDTO.getTitle());
        menuItem.setDescription(menuItemPostDTO.getDescription());
        menuItem.setImgURL(menuItemPostDTO.getImgURL());

        return menuItem;
    }

}
