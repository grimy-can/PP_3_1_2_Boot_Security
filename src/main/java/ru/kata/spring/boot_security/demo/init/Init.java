package ru.kata.spring.boot_security.demo.init;

import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.model.RegistrationForm;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;


@Component
@PropertySource("classpath:application.properties")
public class Init {
    private static final Logger logger = Logger.getLogger(Init.class.getName());
    private String lexicon;
    private int usersNumber;
    private final java.util.Random rand = new java.util.Random();
    private final DateTimeFormatter formatter;
    private final UserServiceImpl userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private Environment environment;


    public Init(DateTimeFormatter formatter,
                UserServiceImpl service,
                RoleRepository roleRepository,
                PasswordEncoder passwordEncoder,
                UserRepository userRepository,
                Environment environment) {
        this.formatter = formatter;
        this.userService = service;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.environment = environment;
    }


    @PostConstruct
    public void createFakeUsers() {
        if (Boolean.parseBoolean(environment.getProperty("fake-users.creator.enabled"))) {
            Init creator = new Init(
                    formatter,
                    userService,
                    roleRepository,
                    passwordEncoder,
                    userRepository,
                    environment);
            creator.create();
        }
    }

    @Transactional
    public void create() {

        lexicon = environment.getProperty("fake-users.creator.lexicon");
        usersNumber = Integer.parseInt(Objects.requireNonNull(environment.getProperty("fake-users.creator.number")));

        createRolesIfNotFound();
        long start = System.currentTimeMillis();
        for (int i = 0; i < this.usersNumber; i++ ) {
            RegistrationForm userForm = getRegistrationForm();
            userService.save(userForm);
        }
        long finish = System.currentTimeMillis();

        logger.info(String.format("создано фейк-юзеров: %s",  this.usersNumber));
        logger.info(String.format("создание одного занимает: %s мс.",  (finish - start) / usersNumber));
        createAdminIfNotFound();
    }

    private RegistrationForm getRegistrationForm() {

        String firstName = getRandomName();
        String lastName = getRandomName();
        String username = String.format("%s%s@mail.ru", firstName, lastName).toLowerCase();
        RegistrationForm userForm = new RegistrationForm();
        userForm.setName(firstName);
        userForm.setLastName(lastName);
        userForm.setEmail(username);
        userForm.setPassword("user");
        userForm.setAge(username.length() * 2);
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


    public void createRolesIfNotFound() {
        String[] roleList = environment.getProperty("fake-users.creator.roles", String[].class);
        System.out.println("createRolesIfNotFound " + roleList);
        for (String roleName : roleList) {
            Role role = roleRepository.findByName(roleName);
            if (role == null) {
                role = new Role(roleName);
                roleRepository.save(role);
            }
        }
    }

    public void createAdminIfNotFound() {
        String username = environment.getProperty("fake-users.creator.admin.username");
        Optional<User> admin = userRepository.findByUsername(username);
        if(admin.isEmpty()) {
            String password = environment.getProperty("fake-users.creator.admin.password");
            Role adminRole = roleRepository.findByName("ROLE_ADMIN");
            RegistrationForm adminForm = new RegistrationForm();
            adminForm.setEmail(username);
            adminForm.setPassword(password);
            adminForm.setName("Admin");
            adminForm.setLastName("Temp");
            adminForm.setAge(100);
            User initAdmin = adminForm.toUser(passwordEncoder);
            initAdmin.setRoles(Arrays.asList(adminRole));
            initAdmin.setRegdate(formatter.format(LocalDateTime.now()));
            userRepository.save(initAdmin);
            logger.info(String.format("admin username: %s, admin password: %s",  username, password));
        }
    }
}
