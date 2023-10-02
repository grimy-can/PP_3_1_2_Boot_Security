package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.model.RegistrationForm;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {

    void save(RegistrationForm form);

    void updateUser(User user);

    Optional<User> getUser(Integer id);

    void deleteUser(User user);

    List<User> getUsers();
}