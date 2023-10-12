package ru.kata.spring.boot_security.demo.controllers;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;


@Controller
@AllArgsConstructor
@RequestMapping("/")
public class UserController {

    private static final String DATE_TIME = "localDateTime";
    private UserServiceImpl service;


    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute(DATE_TIME, LocalDateTime.now());
        return "index";
    }


    @GetMapping("/hello")
    public String getHello(Model model) {
        model.addAttribute(DATE_TIME, LocalDateTime.now());
        return "hello";
    }


    @GetMapping("/authenticated")
    public String getUserPage(Model model, Principal principal) {
        Optional<User> user = service.findUserByUsername(principal.getName());
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "profile";
        }
        else return "login";
    }
}
