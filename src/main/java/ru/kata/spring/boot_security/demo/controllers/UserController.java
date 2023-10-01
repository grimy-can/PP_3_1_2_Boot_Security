package ru.kata.spring.boot_security.demo.controllers;


import lombok.AllArgsConstructor;
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

    @GetMapping(value = "/")
    public String getIndex() {
        return "index";
    }

    @GetMapping(value = "/users")
    public String getUsers(ModelMap model, @RequestParam(value = "count", required = false ) String number) {
        model.addAttribute("users", service.getUsers());
        return "users";
    }

    @GetMapping("/users/{id}")
    public String getUser(Model model, @PathVariable("id") int id) {
        Optional<User> optionalUser = service.getUser(id);
        if (!optionalUser.isPresent()) {
            return "error";
        }
        model.addAttribute("user", optionalUser.get());
        return "user";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user) {
        service.addUser(user);
        return "redirect:/users";
    }

    @PostMapping("/edit_user")
    public String getEditUserPage(@RequestParam(value = "id") String id, Model model) {

        Optional<User> optionalUser = service.getUser(Integer.valueOf(id));
        if (!optionalUser.isPresent()) {
            return "error";
        }
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("user", optionalUser.get());
        return "edit";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        service.updateUser(user);
        return "redirect:/users";
    }


    @PostMapping("/del_user")
    public String deleteUser(@RequestParam(value = "id") String id) {
        Optional<User> optionalUser = service.getUser(Integer.parseInt(id));
        service.deleteUser(optionalUser.get());
        return "redirect:/users";
    }

}
