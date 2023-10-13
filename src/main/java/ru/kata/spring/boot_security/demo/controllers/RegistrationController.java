package ru.kata.spring.boot_security.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.RegistrationForm;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;


@Controller
@AllArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private UserServiceImpl userService;


    @ModelAttribute("form")
    public RegistrationForm userRegistrationForm() {
        return new RegistrationForm();
    }


    @PostMapping
    public String processRegistration(@ModelAttribute("form") @Valid RegistrationForm form,
                                      BindingResult result,
                                      Principal principal,
                                      ModelMap model) {

        Optional<User> admin = userService.findUserByUsername(principal.getName());
        model.addAttribute("admin", admin.get());

        if (result.hasErrors()) {
            model.addAttribute("new_user", "error");
            model.addAttribute("users", userService.getUsers());
            return "users";
        }
        if (!userService.save(form)) {
            model.addAttribute("new_user", "error");
            result.rejectValue("email", null, "Email exist");
            model.addAttribute("users", userService.getUsers());
            return "users";
        }
        model.addAttribute("new_user", form.getEmail());
        model.addAttribute("users", userService.getUsers());
        return "users";
    }
}
