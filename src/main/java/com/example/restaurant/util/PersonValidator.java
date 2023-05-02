package com.example.restaurant.util;

import com.example.restaurant.models.Person;
import com.example.restaurant.services.PersonDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonDetailsService personDetailsService;

    @Autowired
    public PersonValidator(PersonDetailsService personDetailsService) {
        this.personDetailsService = personDetailsService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        Person person = (Person)target;

        if (personDetailsService.checkExistenceByLogin(person.getLogin())) {
            errors.rejectValue("login", "",
                    "Человек с таким именем пользователя уже существует");
        }
    }


}
