package com.example.restaurant.controllers;

import com.example.restaurant.DTO.MenuItemGetDTO;
import com.example.restaurant.DTO.MenuOrderGetDTO;
import com.example.restaurant.DTO.MenuOrderPostDTO;
import com.example.restaurant.models.MenuItem;
import com.example.restaurant.models.MenuOrder;
import com.example.restaurant.services.MenuOrdersService;
import com.example.restaurant.util.MenuOrderErrorResponse;
import com.example.restaurant.util.MenuOrderNotCreatedException;
import com.example.restaurant.util.MenuOrderNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/MenuOrders")
public class MenuOrdersController {

    private final MenuOrdersService menuOrdersService;

    public MenuOrdersController(MenuOrdersService menuOrdersService) {
        this.menuOrdersService = menuOrdersService;
    }

    @GetMapping()
    public List<MenuOrderGetDTO> getOrders(@RequestParam(value = "person_id") Optional<String> person_id) {
        if (person_id.isEmpty()) {
            return menuOrdersService.findAll().stream().map(this::convertToMenuOrderGetDTO)
                    .collect(Collectors.toList());
        }
        else {
            int id = Integer.parseInt(person_id.get()); // Possible NumberFormatException is handled by
            return menuOrdersService.findByPersonId(id).stream().map(this::convertToMenuOrderGetDTO)
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/{id}")
    public MenuOrderGetDTO getOrder(@PathVariable("id") int id) {
        return convertToMenuOrderGetDTO(menuOrdersService.findOne(id));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MenuOrderPostDTO menuOrderPostDTO,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors) {
                errorMessage.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new MenuOrderNotCreatedException(errorMessage.toString());
        }

        menuOrdersService.save(menuOrderPostDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        menuOrdersService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    @ExceptionHandler
    private ResponseEntity<MenuOrderErrorResponse> handleException(MenuOrderNotFoundException ignoredE) {
        MenuOrderErrorResponse response = new MenuOrderErrorResponse(
                "Order with this id was not found",
                System.currentTimeMillis()
        );

        // В HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND - 404 статус
    }

    @ExceptionHandler
    private ResponseEntity<MenuOrderErrorResponse> handleException(MenuOrderNotCreatedException e) {
        MenuOrderErrorResponse response = new MenuOrderErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<MenuOrderErrorResponse> handleException(NumberFormatException e) {
        MenuOrderErrorResponse response = new MenuOrderErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    private MenuOrderGetDTO convertToMenuOrderGetDTO(MenuOrder menuOrder) {
        MenuOrderGetDTO menuOrderGetDTO = new MenuOrderGetDTO();
        menuOrderGetDTO.setId(menuOrder.getId());
        menuOrderGetDTO.setOrderedAt(menuOrder.getOrderedAt());
        menuOrderGetDTO.setPersonId(menuOrder.getCustomer().getId());

        List<MenuItemGetDTO> menuItemsGetDTO = new ArrayList<>();
        MenuItemGetDTO menuItemGetDTO;
        for(MenuItem menuItem: menuOrder.getMenuItems()) {
            menuItemGetDTO = new MenuItemGetDTO();
            menuItemGetDTO.setDescription(menuItem.getDescription());
            menuItemGetDTO.setTitle(menuItem.getTitle());
            menuItemGetDTO.setId(menuItem.getId());
            menuItemsGetDTO.add(menuItemGetDTO);
        }

        menuOrderGetDTO.setMenuItems(menuItemsGetDTO);

        return menuOrderGetDTO;
    }

}
