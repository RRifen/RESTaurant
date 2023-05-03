package com.example.restaurant.controllers;

import com.example.restaurant.DTO.MenuItemGetDTO;
import com.example.restaurant.DTO.MenuItemPostDTO;
import com.example.restaurant.services.MenuItemService;
import com.example.restaurant.util.*;
import com.example.restaurant.util.exceptions.ErrorResponse;
import com.example.restaurant.util.exceptions.MenuItemNotCreatedException;
import com.example.restaurant.util.exceptions.MenuItemNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/MenuItems")
public class MenuItemsController {

    private final MenuItemService menuItemService;
    private final ConverterToDTO converterToDTO;

    @Autowired
    public MenuItemsController(MenuItemService menuItemService, ConverterToDTO converterToDTO) {
        this.menuItemService = menuItemService;
        this.converterToDTO = converterToDTO;
    }

    @GetMapping()
    public List<MenuItemGetDTO> getMenuItems() {
        return menuItemService.findAll().stream().map(converterToDTO::convertToMenuItemGetDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MenuItemGetDTO getItem(@PathVariable("id") int id) {
        return converterToDTO.convertToMenuItemGetDTO(menuItemService.findOne(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MenuItemPostDTO menuItemPostDTO,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();

            List<FieldError> errors = bindingResult.getFieldErrors();
            for(FieldError error: errors) {
                errorMessage.append(error.getField())
                        .append(" - ").append(error.getDefaultMessage())
                        .append(";");
            }

            throw new MenuItemNotCreatedException(errorMessage.toString());
        }

        menuItemService.save(menuItemPostDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        menuItemService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MenuItemNotFoundException ignoredE) {
        ErrorResponse response = new ErrorResponse(
                "MenuItem with this id was not found",
                System.currentTimeMillis()
        );

        // В HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND - 404 статус
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MenuItemNotCreatedException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // TODO - Вынести в отдельный метод
    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(NumberFormatException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
