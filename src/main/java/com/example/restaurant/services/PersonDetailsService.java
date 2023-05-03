package com.example.restaurant.services;

import com.example.restaurant.models.Person;
import com.example.restaurant.repositories.PeopleRepository;
import com.example.restaurant.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PersonDetailsService implements UserDetailsService {

    private final PeopleRepository peopleRepository;

    @Autowired
    public PersonDetailsService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> person = peopleRepository.findByLogin(username);

        if (person.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new PersonDetails(person.get());
    }

    public boolean checkExistenceByLogin(String login) {
        return peopleRepository.findByLogin(login).isPresent();
    }

    public boolean checkCoincidenceByUsername(String usernameCurrent, String usernameToFind) {
        Optional<Person> personOptional = peopleRepository.findByLogin(usernameToFind);
        if (personOptional.isPresent()) {
            Person person = personOptional.get();
            return person.getLogin().equals(usernameCurrent);
        }
        return false;
    }

    public Optional<Person> findOneByUsername(String username) {
        return peopleRepository.findByLogin(username);
    }


}
