package ru.kata.spring.boot_security.demo.service;


import lombok.AllArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import ru.kata.spring.boot_security.demo.model.RegistrationForm;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;


@Service
@AllArgsConstructor
@PropertySource("classpath:application.properties")
public class UserServiceImpl implements UserService {
    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private DateTimeFormatter formatter;
    private final UserRepository userRepository;
    private RoleServiceImpl roleService;
    private PasswordEncoder passwordEncoder;


    @Override
    public boolean save(RegistrationForm form) {
        User newUser = form.toUser(passwordEncoder);
        if (userRepository.findByUsername(newUser.getUsername()).isEmpty()) {

            Role role = roleService.findByName("ROLE_USER");
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
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    @Transactional
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = findUserByUsername(username);
        if (user.isEmpty()) {
            throw new  UsernameNotFoundException(String.format("user %s not found", user.get().getUsername()));
        }
        logger.info("founded: " + user.get().getUsername() + user.get().getAuthorities());
        return new org.springframework.security.core.userdetails
                .User(username, user.get().getPassword(), user.get().getAuthorities());
    }

    @Override
    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        logger.info("founded: " + user.get().getUsername());
        return user;
    }

    @Override
    public void updateUser(User user, MultiValueMap<String, List<String>> roleOptions) {
        List<Role> rolesList = new ArrayList<>();
        for (Object role : roleOptions.get("options")) {
            rolesList.add(roleService.findByName(role.toString()));
        }

        user.setRoles(rolesList);
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
    public List<User> getUsers() {
        List<User> list = userRepository.findAll();
        logger.info("founded: " + list.size());
        return list;
    }


    private Role getUserRole(){
        Role role = new Role();
        role.setName("ROLE_USER");
        return roleService.save(role);
    }

}