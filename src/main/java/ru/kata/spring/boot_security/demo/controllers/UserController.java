package ru.kata.spring.boot_security.demo.controllers;


import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Controller
@AllArgsConstructor
@RequestMapping("/")
public class UserController {

    private UserServiceImpl service;

    @GetMapping
    public String getIndex(Model model) {
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "index";
    }

    @GetMapping("/authenticated")
    public String getUserPage(Model model, Principal principal) {
        Optional<User> user = service.findUserByUsername(principal.getName());
        model.addAttribute("user", user.get());
        return "profile";
    }


    @GetMapping(value = "/admin/users")
    public String getUsers(ModelMap model, Principal princip) {
        model.addAttribute("princip", princip.getName());
        model.addAttribute("users", service.getUsers());
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "users";
    }


    @GetMapping("/admin/users/{id}")
    public String getUser(Model model, @PathVariable("id") long id) {
        Optional<User> optionalUser = service.getUserById(id);
        model.addAttribute("user", optionalUser.get());
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "user";
    }


    @PostMapping("/admin/edit_user")
    public String getEditUserPage(@RequestParam(value = "id") Long id, Model model) {
        Optional<User> optionalUser = service.getUserById(id);
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("roles", optionalUser.get().getAuthorities().toArray(new GrantedAuthority[2]));
        model.addAttribute("user", optionalUser.get());
        return "edit";
    }

    @PostMapping("/admin/update")
    public String updateUser(@ModelAttribute("user") User user,
                             @RequestBody MultiValueMap<String,List<String>> roleOptions) {
        service.updateUser(user, roleOptions);
        return "redirect:/admin/users";
    }


    @PostMapping("/admin/del_user")
    public String deleteUser(@RequestParam(value = "id") String id) {
        Optional<User> optionalUser = service.getUserById(Long.parseLong(id));
        service.deleteUser(optionalUser.get());
        return "redirect:/admin/users";
    }

}
