package com.example.restaurant.controllers;

import com.example.restaurant.DTO.MenuOrderGetDTO;
import com.example.restaurant.DTO.MenuOrderPostDTO;
import com.example.restaurant.services.MenuOrdersService;
import com.example.restaurant.util.ConverterToDTO;
import com.example.restaurant.util.ErrorResponse;
import com.example.restaurant.util.MenuOrderNotCreatedException;
import com.example.restaurant.util.MenuOrderNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/MenuOrders")
public class MenuOrdersController {

    private final MenuOrdersService menuOrdersService;
    private final ConverterToDTO converterToDTO;

    public MenuOrdersController(MenuOrdersService menuOrdersService, ConverterToDTO converterToDTO) {
        this.menuOrdersService = menuOrdersService;
        this.converterToDTO = converterToDTO;
    }

    @GetMapping()
    public List<MenuOrderGetDTO> getOrders(@RequestParam(value = "person_id") Optional<String> person_id) {
        if (person_id.isEmpty()) {
            return menuOrdersService.findAll().stream().map(converterToDTO::convertToMenuOrderGetDTO)
                    .collect(Collectors.toList());
        }
        else {
            int id = Integer.parseInt(person_id.get()); // Possible NumberFormatException is handled by
            return menuOrdersService.findByPersonId(id).stream().map(converterToDTO::convertToMenuOrderGetDTO)
                    .collect(Collectors.toList());
        }
    }

    @GetMapping("/{id}")
    public MenuOrderGetDTO getOrder(@PathVariable("id") int id) {
        return converterToDTO.convertToMenuOrderGetDTO(menuOrdersService.findOne(id));
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
    private ResponseEntity<ErrorResponse> handleException(MenuOrderNotFoundException ignoredE) {
        ErrorResponse response = new ErrorResponse(
                "Order with this id was not found",
                System.currentTimeMillis()
        );

        // В HTTP ответе тело ответа (response) и статус в заголовке
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND); // NOT_FOUND - 404 статус
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MenuOrderNotCreatedException e) {
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

}
