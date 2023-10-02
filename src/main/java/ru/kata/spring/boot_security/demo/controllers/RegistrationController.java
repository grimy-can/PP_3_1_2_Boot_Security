package ru.kata.spring.boot_security.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.RegistrationForm;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repository.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@AllArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private UserService userService;


    @ModelAttribute("user")
    public RegistrationForm userRegistrationForm() {
        return new RegistrationForm();
    }

    @GetMapping
    public String getRegisterForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "registration";
    }


    @PostMapping
    public String processRegistration(@Valid RegistrationForm form,  Errors errors) {
        if (errors.hasErrors()) {
            return "registration";
        }
        userService.save(form);
        return "redirect:/registration?success";
    }

}
