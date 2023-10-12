package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.util.MultiValueMap;
import ru.kata.spring.boot_security.demo.model.RegistrationForm;
import ru.kata.spring.boot_security.demo.model.User;
import java.util.List;
import java.util.Optional;


public interface UserService extends UserDetailsService {

    boolean save(RegistrationForm form);

    void updateUser(User user, MultiValueMap<String, List<String>> roleOptions);

    Optional<User> findUserByUsername(String username);

    Optional<User> getUserById(Long id);

    void deleteUser(User user);

    List<User> getUsers();
}