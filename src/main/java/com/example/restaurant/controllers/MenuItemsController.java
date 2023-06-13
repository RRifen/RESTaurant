package com.example.restaurant.controllers;

import com.example.restaurant.DTO.MenuItemGetDTO;
import com.example.restaurant.DTO.MenuItemPostDTO;
import com.example.restaurant.services.MenuItemsService;
import com.example.restaurant.util.*;
import com.example.restaurant.util.ErrorResponse;
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

    private final MenuItemsService menuItemsService;
    private final ConverterToDTO converterToDTO;

    @Autowired
    public MenuItemsController(MenuItemsService menuItemsService, ConverterToDTO converterToDTO) {
        this.menuItemsService = menuItemsService;
        this.converterToDTO = converterToDTO;
    }

    @GetMapping()
    @CrossOrigin(allowCredentials = "true", origins = "http://localhost:3000", exposedHeaders = {"Access-Control-Allow-Origin","Access-Control-Allow-Credentials"})
    public List<MenuItemGetDTO> getMenuItems() {
        return menuItemsService.findAll().stream().map(converterToDTO::convertToMenuItemGetDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public MenuItemGetDTO getItem(@PathVariable("id") int id) {
        return converterToDTO.convertToMenuItemGetDTO(menuItemsService.findOne(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MenuItemPostDTO menuItemPostDTO,
                                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            generateValidationErrorMessage(bindingResult);
        }

        menuItemsService.save(menuItemPostDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id) {
        menuItemsService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> updateMenuItem(@PathVariable("id") int id,
                                                     @RequestBody @Valid MenuItemPostDTO menuItemPostDTO,
                                                     BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            generateValidationErrorMessage(bindingResult);
        }

        menuItemsService.update(menuItemPostDTO, id);

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

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(NumberFormatException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    private static void generateValidationErrorMessage(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();

        List<FieldError> errors = bindingResult.getFieldErrors();
        for(FieldError error: errors) {
            errorMessage.append(error.getField())
                    .append(" - ").append(error.getDefaultMessage())
                    .append(";");
        }

        throw new MenuItemNotCreatedException(errorMessage.toString());
    }
}
