package ru.kata.spring.boot_security.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.kata.spring.boot_security.demo.model.RegistrationForm;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import javax.validation.Valid;
import java.time.LocalDateTime;


@Controller
@AllArgsConstructor
@RequestMapping("/registration")
public class RegistrationController {

    private UserServiceImpl userService;


    @ModelAttribute("form")
    public RegistrationForm userRegistrationForm() {
        return new RegistrationForm();
    }


    @GetMapping
    public String getRegisterForm(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("form", new RegistrationForm());
        return "registration";
    }


    @PostMapping
    public String processRegistration(@ModelAttribute("form") @Valid RegistrationForm form,
                                      BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }
        if (!userService.save(form)) {
            bindingResult.rejectValue("email", null,
                    "Email существует в базе данных");
            return "registration";
        }
        return "redirect:/registration?success";
    }
}
