package ru.kata.spring.boot_security.demo.controllers;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import java.time.LocalDateTime;
import java.util.Optional;


@Controller
@AllArgsConstructor
@RequestMapping("/")
public class UserController {

    private UserService service;

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "index";
    }


    @GetMapping(value = "/admin/users")
    public String getUsers(ModelMap model) {
        model.addAttribute("users", service.getUsers());
        return "users";
    }


    @GetMapping("/admin/users/{id}")
    public String getUser(Model model, @PathVariable("id") long id) {
        Optional<User> optionalUser = service.getUserById(id);
        if (!optionalUser.isPresent()) {
            return "error";
        }
        model.addAttribute("user", optionalUser.get());
        return "user";
    }


    @GetMapping("/authenticated/user/{id}")
    public String getUserPage(Model model, @PathVariable("id") long id) {
        Optional<User> optionalUser = service.getUserById(id);
        if (!optionalUser.isPresent()) {
            return "error";
        }
        model.addAttribute("user", optionalUser.get());
        return "user";
    }


    @PostMapping("/admin/edit_user")
    public String getEditUserPage(@RequestParam(value = "id") String id, Model model) {

        Optional<User> optionalUser = service.getUserById(Long.valueOf(id));
        if (!optionalUser.isPresent()) {
            return "error";
        }
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("user", optionalUser.get());
        return "edit";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") User user) {
        service.updateUser(user);
        return "redirect:/admin/users";
    }


    @PostMapping("/admin/del_user")
    public String deleteUser(@RequestParam(value = "id") String id) {
        Optional<User> optionalUser = service.getUserById(Long.parseLong(id));
        service.deleteUser(optionalUser.get());
        return "redirect:/admin/users";
    }

}
