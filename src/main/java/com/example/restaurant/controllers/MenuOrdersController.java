package com.example.restaurant.controllers;

import com.example.restaurant.DTO.MenuOrderGetDTO;
import com.example.restaurant.DTO.MenuOrderPostDTO;
import com.example.restaurant.models.Person;
import com.example.restaurant.services.MenuOrdersService;
import com.example.restaurant.services.PeopleService;
import com.example.restaurant.util.ConverterToDTO;
import com.example.restaurant.util.ErrorResponse;
import com.example.restaurant.util.exceptions.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    private final PeopleService peopleService;
    private final ConverterToDTO converterToDTO;

    public MenuOrdersController(MenuOrdersService menuOrdersService, PeopleService peopleService, ConverterToDTO converterToDTO) {
        this.menuOrdersService = menuOrdersService;
        this.peopleService = peopleService;
        this.converterToDTO = converterToDTO;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<MenuOrderGetDTO> getOrders(@RequestParam(value = "id", required = false) String id) {
        if (id == null) {
            return menuOrdersService.findAll().stream().map(converterToDTO::convertToMenuOrderGetDTO)
                    .collect(Collectors.toList());
        }

       int idInt = Integer.parseInt(id);
       return converterToDTO.convertToMenuOrdersGetDTO(menuOrdersService.findByPersonId(idInt));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public MenuOrderGetDTO getOrder(@PathVariable("id") int id) {
        return converterToDTO.convertToMenuOrderGetDTO(menuOrdersService.findOne(id));
    }

    @GetMapping("/current")
    public List<MenuOrderGetDTO> getCurrentOrder(Authentication authentication) {
        if (authentication == null) {
            throw new AuthenticationRequiredException();
        }
        Optional<Person> optionalPerson = peopleService.findByLogin(authentication.getName());
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            return converterToDTO.convertToMenuOrdersGetDTO(
                    menuOrdersService.findByPersonId(
                            person.getId()));
        }
        else {
            return new ArrayList<>();
        }
    }

    @PostMapping
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid MenuOrderPostDTO menuOrderPostDTO,
                                            BindingResult bindingResult,
                                            Authentication authentication) {
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

        if (authentication == null) {
            throw new AuthenticationRequiredException();
        }
        String name = authentication.getName();
        Person person = peopleService.findByLogin(name).orElseThrow(BadUsername::new);
        if (!person.getRole().equals("ROLE_ADMIN")) {
            if (person.getId() != menuOrderPostDTO.getPersonId()) {
                throw new AccessDeniedException("Access denied");
            }
        }

        menuOrdersService.save(menuOrderPostDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") int id,
                                             Authentication authentication) {
        if (authentication == null) {
            throw new AuthenticationRequiredException();
        }
        String name = authentication.getName();
        Person person = peopleService.findByLogin(name).orElseThrow(BadUsername::new);
        if (!person.getRole().equals("ROLE_ADMIN")) {
            if (person.getId() != menuOrdersService.findOne(id).getCustomer().getId()) {
                throw new AccessDeniedException("Access denied");
            }
        }
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

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(AuthenticationRequiredException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(BadUsername e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                System.currentTimeMillis()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
