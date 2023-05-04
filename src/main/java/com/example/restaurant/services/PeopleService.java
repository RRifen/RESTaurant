package com.example.restaurant.services;

import com.example.restaurant.models.Person;
import com.example.restaurant.repositories.PeopleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }


    public Optional<Person> findByLogin(String login) {
        return peopleRepository.findByLogin(login);
    }

}
