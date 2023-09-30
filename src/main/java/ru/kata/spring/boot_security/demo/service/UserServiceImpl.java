package ru.kata.spring.boot_security.demo.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class.getName());
    private DateTimeFormatter formatter;
    private final UserRepository userRepository;


    @Override
    public void addUser(User user) {
        user.setRegdate(formatter.format(LocalDateTime.now()));
        userRepository.save(user);
        logger.info("new user: " + user.getAlias());
    }

    @Override
    public void updateUser(User user) {
        user.setEdited(formatter.format(LocalDateTime.now()));
        userRepository.save(user);
        logger.info("updated: " + user.getAlias());
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
        logger.info("deleted: " + user.getAlias());
    }

    @Override
    public Optional<User> getUser(Integer id) {
        Optional<User> user = Optional.ofNullable(userRepository.getOne(id));
        logger.info("founded: " + user.get().getAlias());
        return user;
    }


    @Override
    public List<User> getUsers() {
        List<User> list = userRepository.findAll();
        logger.info("founded: " + list.size());
        return list;
    }
}