package ru.kata.spring.boot_security.demo.controllers;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.RegistrationForm;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Controller
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private static final String DATE_TIME = "localDateTime";
    private UserServiceImpl service;

    @GetMapping(value = "/users")
    public String getUsers(ModelMap model, Principal principal) {
        Optional<User> admin = service.findUserByUsername(principal.getName());
        model.addAttribute("admin", admin.get());
        model.addAttribute("form", new RegistrationForm());
        model.addAttribute("users", service.getUsers());
        model.addAttribute(DATE_TIME, LocalDateTime.now());
        return "users";
    }


    @GetMapping("/users/{id}")
    public String getUser(Model model, @PathVariable("id") long id) {
        Optional<User> optionalUser = service.getUserById(id);
        model.addAttribute("user", optionalUser.get());
        model.addAttribute(DATE_TIME, LocalDateTime.now());
        return "user";
    }


    @PostMapping("/edit_user")
    public String getEditUserPage(@RequestParam(value = "id") Long id, Model model) {
        Optional<User> optionalUser = service.getUserById(id);
        model.addAttribute(DATE_TIME, LocalDateTime.now());
        model.addAttribute("roles", optionalUser.get().getAuthorities().toArray(new GrantedAuthority[2]));
        model.addAttribute("user", optionalUser.get());
        return "edit";
    }


    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestBody MultiValueMap<String, List<String>> roleOptions) {
        service.updateUser(user, roleOptions);
        return "redirect:/admin/users";
    }


    @PostMapping("/del_user")
    public String deleteUser(@RequestParam(value = "id") String id) {
        Optional<User> optionalUser = service.getUserById(Long.parseLong(id));
        service.deleteUser(optionalUser.get());
        return "redirect:/admin/users";
    }
}
