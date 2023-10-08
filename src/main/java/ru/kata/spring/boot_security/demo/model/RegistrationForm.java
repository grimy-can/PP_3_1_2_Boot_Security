package ru.kata.spring.boot_security.demo.model;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.validation.constraints.*;


@Data
public class RegistrationForm {

    @NotNull
    @Pattern(regexp = "^\\S+@\\S+\\.\\S+$")
    private String username;
    @NotNull
    @Size(min=8, max=32)
    private String password;

    @NotNull
    private String name;

    @NotNull
    private String lastName;

    @NotNull
    @Min(value = 18, message = "18+")
    private int age;


    public User toUser(PasswordEncoder passwordEncoder) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setName(name);
        newUser.setLastName(lastName);
        newUser.setAge(age);
        return newUser;
    }
}
