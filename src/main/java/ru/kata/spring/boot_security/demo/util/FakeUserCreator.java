package ru.kata.spring.boot_security.demo.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.RegistrationForm;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.logging.Logger;

public class FakeUserCreator {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    final String lexicon = "abcdefghijklmnopqrstuvwxyz";
    final java.util.Random rand = new java.util.Random();
    private final DateTimeFormatter formatter;
    private final UserServiceImpl userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    int UsersNumber;

    public FakeUserCreator(DateTimeFormatter formatter, UserServiceImpl service,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder, UserRepository userRepository,
                           int usersNumber) {
        this.formatter = formatter;
        this.userService = service;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        UsersNumber = usersNumber;
    }

    @Transactional
    public void create() {

        createUserRoleIfNotFound();
        createAdminRoleIfNotFound();

        for (int i = 0; i < this.UsersNumber; i++ ) {
            RegistrationForm userForm = getRegistrationForm();
            userService.save(userForm);
        }
        logger.info(String.format("создано фейк-юзеров: %s",  this.UsersNumber));
    }

    private RegistrationForm getRegistrationForm() {
        String firstName = getRandomName();
        String lastName = getRandomName();
        String username = String.format("%s%s@mail.ru", firstName, lastName).toLowerCase();
        RegistrationForm userForm = new RegistrationForm();
        userForm.setName(firstName);
        userForm.setLastName(lastName);
        userForm.setUsername(username);
        userForm.setPassword(String.valueOf(username.hashCode()));
        userForm.setAge(username.length() * 3);
        return userForm;
    }

    public String getRandomName() {
        StringBuilder builder = new StringBuilder();
        while(builder.toString().length() == 0) {
            int length = rand.nextInt(5) + 5;
            for(int i = 0; i < length; i++) {
                builder.append(lexicon.charAt(rand.nextInt(lexicon.length())));
            }
        }
        return builder.toString().substring(0, 1).toUpperCase() + builder.toString().substring(1);
    }


    public void createUserRoleIfNotFound() {

        Role role = roleRepository.findByName("ROLE_USER");
        if (role == null) {
            role = new Role("ROLE_USER");
            roleRepository.save(role);
        }
    }

    public void createAdminRoleIfNotFound() {

        Role role = roleRepository.findByName("ROLE_ADMIN");
        if (role == null) {
            role = new Role("ROLE_ADMIN");
            roleRepository.save(role);
            RegistrationForm adminForm = new RegistrationForm();
            adminForm.setUsername("admin@gmail.com");
            adminForm.setPassword("admin");
            adminForm.setName("Admin");
            adminForm.setLastName("Admin");
            adminForm.setAge(100);
            User admin = adminForm.toUser(passwordEncoder);
            admin.setRoles(Arrays.asList(role));
            admin.setRegdate(formatter.format(LocalDateTime.now()));
            userRepository.save(admin);
        }
    }
}
