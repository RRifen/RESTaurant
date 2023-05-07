package com.example.restaurant.controllers;

import com.example.restaurant.models.Person;
import com.example.restaurant.services.RegistrationService;
import com.example.restaurant.util.ErrorResponse;
import com.example.restaurant.util.PersonValidator;
import com.example.restaurant.util.exceptions.LoginAlreadyTakenException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AuthController {

    private final RegistrationService registrationService;
    private final PersonValidator personValidator;

    @Autowired
    public AuthController(RegistrationService registrationService, PersonValidator personValidator) {
        this.registrationService = registrationService;
        this.personValidator = personValidator;
    }

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<HttpStatus> perform() {
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @GetMapping("/login")
    @ResponseBody
    public ResponseEntity<HttpStatus> performLogin() {
        return ResponseEntity.ok(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/fail_login")
    @ResponseBody
    public ResponseEntity<ErrorResponse> loginFail() {
        ErrorResponse response = new ErrorResponse(
                "Wrong login or password",
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/registration")
    @ResponseBody
    public ResponseEntity<HttpStatus> performRegistration(@Valid Person person,
                                                          BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);

        if (bindingResult.hasErrors()) {
            generateValidationErrorMessage(bindingResult);
        }

        registrationService.register(person);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(LoginAlreadyTakenException e) {
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

        throw new LoginAlreadyTakenException(errorMessage.toString());
    }
}
