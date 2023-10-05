package ru.kata.spring.boot_security.demo.service;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.RegistrationForm;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.util.FakeUserCreator;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
@PropertySource("classpath:application.properties")
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private DateTimeFormatter formatter;
    private final UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private Environment environment;


    @PostConstruct
    public void createFakeUsers() {
        if ( Boolean.parseBoolean(environment.getProperty("fake-users.creator.enabled"))) {
            int number = Integer.parseInt(Objects.requireNonNull(environment.getProperty("fake-users.creator.number")));
            FakeUserCreator creator = new FakeUserCreator(formatter, this, roleRepository, passwordEncoder, userRepository, number);
            creator.create();
        }
    }

    @Override
    public boolean save(RegistrationForm form) {
        User newUser = form.toUser(passwordEncoder);
        if (!userRepository.findByUsername(newUser.getUsername()).isPresent()) {

            Role role = roleRepository.findByName("ROLE_USER");

            if(role == null) {
                role = getUserRole();
                newUser.setRoles(Arrays.asList(role));
            }
            newUser.setRoles(Arrays.asList(role));
            newUser.setRegdate(formatter.format(LocalDateTime.now()));
            userRepository.save(newUser);
            logger.info("новый пользователь: " + newUser.getUsername());
            return true;
        }
        logger.info("попытка повторной регистрации: " + newUser.getUsername());
        return false;

    }

    @Override
    public void updateUser(User user) {
        user.setEdited(formatter.format(LocalDateTime.now()));
        userRepository.save(user);
        logger.info("updated: " + user.getUsername());
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
        logger.info("deleted: " + user.getUsername());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> user = Optional.ofNullable(userRepository.getOne(id));
        logger.info("founded: " + user.get().getUsername());
        return user;
    }


    @Override
    public List<User> getUsers() {
        List<User> list = userRepository.findAll();
        logger.info("founded: " + list.size());
        return list;
    }

    private Role getUserRole(){
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleRepository.save(role);
    }

}